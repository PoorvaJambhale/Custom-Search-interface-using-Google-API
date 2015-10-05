import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class VectorSystem {
	
	private static String delimiter = "\\^";
	
	private Document query;
	private List<Document> documentList;
	private Map<Term, Double> dfMap;
	private Map<Term, Double> idfMap;
	private Set<Term> allTermsInSystem;
	
	public void printDocumentList() {
		System.out.println("Printing document list now");
		for (Document doc : this.getDocumentList()) {
			System.out.println(doc.getDocumentNumber() + " : " + doc.getTextValue());
		}
	}
	
	public void printRerankedDocumentList(JSONArray rerankedArray) throws JSONException {
		System.out.println("Printing reranked list now");
		
		for (int i = 0 ; i < rerankedArray.length() ; i++){
			JSONObject jsonObject = rerankedArray.getJSONObject(i);
			String documentIndex = rerankedArray.getJSONObject(i).getString("documentIndex");
			System.out.println("Document index " + documentIndex);
		}
	}

	public void printQuery() {
		System.out.println("Printing query now");
		System.out.println(this.query.getDocumentNumber() + " : " + query.getTextValue());
	}
	
	public List<Document> getDocumentList() {
		return this.documentList;
	}

	public void setDocumentList(List<Document> documentList) {
		this.documentList = documentList;
	}

	public Map<Term, Double> getDfMap() {
		return this.dfMap;
	}

	public void setDfMap(Map<Term, Double> dfMap) {
		this.dfMap = dfMap;
	}

	public Map<Term, Double> getIdfMap() {
		return this.idfMap;
	}

	public void setIdfMap(Map<Term, Double> idfMap) {
		this.idfMap = idfMap;
	}

	public Set<Term> getAllTermsInSystem() {
		return allTermsInSystem;
	}

	public void setAllTermsInSystem(Set<Term> allTermsInSystem) {
		this.allTermsInSystem = allTermsInSystem;
	}

	public VectorSystem() {
		this.documentList = new ArrayList<Document>();
		this.dfMap = new HashMap<Term, Double>();
		this.idfMap = new HashMap<Term, Double>();
		this.allTermsInSystem = new HashSet<Term>();
	}
	
	public void addQuery(int docNumber, String line) {
		this.query = new Document(docNumber, line, null);
	}
	
	public void addDocument(int docNumber, String line, JSONObject jsonObject) {
		Document doc = new Document(docNumber, line, jsonObject);
		this.documentList.add(doc);
	}
	
	public Set<Term> getAllTermsInAllSystem() {
		if (this.allTermsInSystem.size() == 0) {
			Set<Term> allTermsInSystem = new HashSet<Term>();
			for (Document doc : documentList) {
				allTermsInSystem.addAll(doc.getTerms());
			}
			this.allTermsInSystem = allTermsInSystem;
			return allTermsInSystem;
		} else {
			return this.allTermsInSystem;
		}
	}
	
	public void setupDocumentFrequency() {
		Set<Term> allTermsInSystem = this.getAllTermsInAllSystem();
		for (Term t : allTermsInSystem) {
				double dfValue = 0;
				for (Document doc :this. documentList) {
					if (doc.containsTerm(t)) {
						dfValue++;
					}
				}
				this.dfMap.put(t, dfValue);
		}
	}
	
	public void setupIDF() {
		Set<Term> allTermsInSystem = this.getAllTermsInAllSystem();
		for (Term t : allTermsInSystem) {
			Double dfValue = this.dfMap.get(t);
			Double idfValue = Math.log10(this.documentList.size() / dfValue);
			this.idfMap.put(t, idfValue);
		}
	}
	
	public double getTfidfWeight(Document d, Term t) {
		
		double tfOfTermInDocument = 0;
		if (d.getTfMap().containsKey(t)) {
			tfOfTermInDocument = d.getTfMap().get(t);
		}
		double idfOfTerm = 0;
		if (this.getIdfMap().containsKey(t)) {
			idfOfTerm = this.getIdfMap().get(t);
		}
		double tfidfWeight = tfOfTermInDocument * idfOfTerm;
		return tfidfWeight;
	}
	
	
	public double getCosineSimilarity(Document doc) {

		Set<Term> queryTerms = this.query.getTerms();
		
		double numerator = 0;
		double denominator1 = 0;
		double denominator2 = 0;
		double denominator = 0;
		
		for (Term t : queryTerms) {
			double tfidfWeightOfTermInQuery = this.getTfidfWeight(this.query, t);
			double tfidfWeightOfTermInDocument = this.getTfidfWeight(doc, t);
			numerator += tfidfWeightOfTermInQuery * tfidfWeightOfTermInDocument;
		}
		
		for (Term t : queryTerms) {
			double tfidfWeightOfTermInQuery = this.getTfidfWeight(this.query, t);
			denominator1 += tfidfWeightOfTermInQuery * tfidfWeightOfTermInQuery;
		}
		denominator1 = Math.sqrt(denominator1);
		
		for (Term t : queryTerms) {
			double tfidfWeightOfTermInDocument = this.getTfidfWeight(doc, t);
			denominator2 += tfidfWeightOfTermInDocument * tfidfWeightOfTermInDocument;
		}
		denominator2 = Math.sqrt(denominator2);
		
		denominator = denominator1 * denominator2;
		
		double cosineSimilarity = numerator / denominator;
		
		return cosineSimilarity;
		
	}
	
	public Map<Integer, Double> calculateCosineSimilarity() {
		Map<Integer, Double> cosineSimilarityMap = new HashMap<Integer, Double>();
		for (Document doc : this.getDocumentList()) {
			double cosineValue = this.getCosineSimilarity(doc);
			cosineSimilarityMap.put(doc.getDocumentNumber(), cosineValue);
		}
		return cosineSimilarityMap;
	}

	//Retrieves each element from the itemArray as JSONObject and modifies it to add a documentIndex. This is used later.
	public void setupDocumentIndex(JSONArray itemArray) throws IOException, JSONException {
		int documentNumber = 1;
		for (int i = 0 ; i < itemArray.length() ; i++){
			JSONObject jsonObject = itemArray.getJSONObject(i);
			jsonObject.put("documentIndex", documentNumber);
			documentNumber++;
		}
	}
	
	public void setupQueryAndDocuments(String selectedRecords, JSONArray itemArray) throws IOException, JSONException {
		int documentNumber = 1;
		for (int i = 0 ; i < itemArray.length() ; i++){
			JSONObject jsonObject = itemArray.getJSONObject(i);
			String documentIndex = itemArray.getJSONObject(i).getString("documentIndex");
			String title = itemArray.getJSONObject(i).getString("title");
			String snippet =  itemArray.getJSONObject(i).getString("snippet");
			String strForDocument =  title + " " + snippet;
			this.addDocument(documentNumber, strForDocument, jsonObject);
			documentNumber++;
		}
		
		this.printDocumentList();
		
		String[] queryRecordIndexNumbers = selectedRecords.split(VectorSystem.delimiter);
		String queryStr = "";
		for (int i = 0; i < queryRecordIndexNumbers.length; i++) {
			for (Document doc : this.getDocumentList()) {
				if (doc.getDocumentNumber() == Integer.valueOf(queryRecordIndexNumbers[i])) {
					queryStr += doc.getTextValue();
				}
			}
		}
		this.addQuery(0, queryStr);
		
		this.printQuery();
	}
	
	public int getNextHigestResultIndex(Map<Integer, Double> cosineSimilarityMap) {
		double currentMax = -100;
		int currentMaxIndex = this.getDocumentList().get(0).getDocumentNumber();
		for (Entry e : cosineSimilarityMap.entrySet()) {
			Integer key = (Integer) e.getKey();
			if (cosineSimilarityMap.get(key) > currentMax) {
				currentMax = cosineSimilarityMap.get(key);
				currentMaxIndex = key;
			}
		}
		System.out.println("Max index = " + currentMaxIndex + " Sim value = " + cosineSimilarityMap.get(currentMaxIndex));
		return currentMaxIndex;
	}
	
	public String getRerankedResults(String selectedRecords, String searchResults) throws IOException, JSONException {
		
		JSONObject obj = new JSONObject(searchResults);
		JSONArray itemArray = obj.getJSONArray("items");
		JSONArray rerankedItemArray = new JSONArray();
		
		this.setupDocumentIndex(itemArray);

		this.setupQueryAndDocuments(selectedRecords, itemArray);
	
		this.setupDocumentFrequency();
		
		this.setupIDF();
		
		Map<Integer, Double> cosineSimilarityMap = this.calculateCosineSimilarity();

		for (Entry e : cosineSimilarityMap.entrySet()) {
			System.out.println("Document number: " + e.getKey() + "\tSimilarity: " + e.getValue());
		}
		
		for (int i = 0; i < this.getDocumentList().size(); i++) {
			int documentIndexOfHighestSimilarity = this.getNextHigestResultIndex(cosineSimilarityMap);
			cosineSimilarityMap.put(documentIndexOfHighestSimilarity, Double.valueOf(-100));
			for (Document doc : this.getDocumentList()) {
				if (doc.getDocumentNumber() == documentIndexOfHighestSimilarity) {
					rerankedItemArray.put(i, doc.getJsonObject());
				}
			}
		}
		
		this.printRerankedDocumentList(rerankedItemArray);
		
		obj.put("items", rerankedItemArray);
		
		return obj.toString();
			
	}
}