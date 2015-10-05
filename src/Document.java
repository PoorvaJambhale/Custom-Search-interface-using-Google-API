import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;

public class Document {
	
	private int documentNumber;
	private String actualText;
	private JSONObject jsonObject;
	private Set<Term> terms;
	private Map<Term, Double> tfMap;
	private static Stemmer stemmer;
	
	private static List<String> stopWordsList = new ArrayList<String>();
	
	static {
		stemmer = new Stemmer();
		String[] stopWords = {"a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"};
		stopWordsList = Arrays.asList(stopWords);
	}
	
	public Document(int docNumber, String str, JSONObject jsonObject) {
		
		this.setDocumentNumber(docNumber);
		this.setActualText(str);
		this.setJsonObject(jsonObject);
		
		// Pre-processing - Stemming using Porter's algorithm.
		str = stemmer.getStemmedString(str);
		
		String[] strArray = str.split(" ");
		this.terms = new HashSet<Term>();
		this.tfMap = new HashMap<Term, Double>();
		for (String s : strArray) {
			if (!stopWordsList.contains(s)) {
				Term t = new Term(s);
				this.terms.add(t);
				if (!this.tfMap.containsKey(t)) {
					this.tfMap.put(t, Double.valueOf(1));
				} else {
					this.tfMap.put(t, this.tfMap.get(t) + 1);
				}
			} else {
				System.out.println("Neglecting stop word " + s);
			}
		}
	}
	
	public boolean containsTerm(Term t) {
		for (Term termsInDocument : this.terms) {
			if (termsInDocument.equals(t)) {
				return true;
			}
		}
		return false;
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public int getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(int documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getActualText() {
		return actualText;
	}

	public void setActualText(String actualText) {
		this.actualText = actualText;
	}

	public Set getTerms() {
		return this.terms;
	}

	public Map<Term, Double> getTfMap() {
		return this.tfMap;
	}

	public void setTfMap(Map<Term, Double> tfMap) {
		this.tfMap = tfMap;
	}
	
	public String getTextValue() {
		return this.getActualText();
	}
	
	public String printTerms() {
		String str = "";
		Set<Term> terms = this.getTerms();
		for (Term t : terms) {
			str += t.getTextValue() + " ";
		}
		return str;
	}
}