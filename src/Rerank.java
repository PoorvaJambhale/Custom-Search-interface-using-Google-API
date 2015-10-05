
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.io.*;



public class Rerank extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private static final String delimiter = "\\^";

	public Rerank() {
		super();
	}

	
	private String getSelectedResultsIndexStr(Enumeration paramNames) {
		String returnStr = "";
		while (paramNames.hasMoreElements())
		{
			String paramName =
					(String)paramNames.nextElement();
			String index = paramName.substring(paramName.indexOf("_") + 1);
			returnStr = returnStr + index + "^";
		}
		returnStr = returnStr.substring(0, returnStr.lastIndexOf("^"));
		System.out.println("selectedResultsIndexStr = " + returnStr);
		return returnStr;
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher rd = null; 
		HttpSession session = request.getSession();
		String searchResults = (String) session.getAttribute("searchResults");

		Enumeration paramNames = request.getParameterNames();
		String selectedResultsIndexStr = this.getSelectedResultsIndexStr(paramNames);

		VectorSystem vectorSystem = new VectorSystem();
		String resultStr = "";
		try {
			resultStr = vectorSystem.getRerankedResults(selectedResultsIndexStr, searchResults);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute("todo", resultStr);
		rd = request.getRequestDispatcher("/RerankedResults.jsp");
		rd.forward(request, response);

	}

}
