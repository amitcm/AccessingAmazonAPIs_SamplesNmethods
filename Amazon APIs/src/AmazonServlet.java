

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AmazonServlet
 */
public class AmazonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AmazonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sProduct = request.getParameter("product_name");
		String sMinRating = request.getParameter("min_rating");
		
		if(sProduct == "" || sMinRating == "")
			return;
		ArrayList<String> vManufacturers = new ArrayList<String>();
		ArrayList<Integer> ALRating = new ArrayList<Integer>();
		
		MutableInteger countRecords = new MutableInteger();
		ItemLookupSample.main_lookup(sProduct, sMinRating, vManufacturers, ALRating, Integer.parseInt(sMinRating), countRecords);
		String strTemp = "";
		for(int i = 0; i < countRecords.value; i++){
			strTemp += (vManufacturers.get( i ) + "\t\t---\t\t" + ALRating.get(i) + "<br>");
		}
				
		response.setContentType("text/html");
		   PrintWriter out = response.getWriter();
		   out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " + "Transitional//EN\">\n" +
		               "<HTML>\n" +
		               "<HEAD><TITLE>Product!</TITLE></HEAD>\n" +
		               "<BODY>\n" +
		               "<tr>\n" +
		               strTemp +
		               "</BODY></HTML>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
