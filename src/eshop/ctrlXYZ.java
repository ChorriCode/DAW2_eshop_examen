package eshop;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eshop.beans.Book;
import eshop.beans.CartItem;
import eshop.model.DataManager;
import java.util.Hashtable;
import java.util.List;


public class ctrlXYZ extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	private static final long serialVersionUID = 1L;

	public ctrlXYZ() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		System.out.println("*** initializing controller servlet.");
		super.init(config);

		DataManager dataManager = new DataManager();
		dataManager.setDbURL(config.getInitParameter("dbURL"));
		dataManager.setDbUserName(config.getInitParameter("dbUserName"));
		dataManager.setDbPassword(config.getInitParameter("dbPassword"));

		ServletContext context = config.getServletContext();
		context.setAttribute("base", config.getInitParameter("base"));
		context.setAttribute("imageURL", config.getInitParameter("imageURL"));
		context.setAttribute("dataManager", dataManager);

		try { // load the database JDBC driver
			Class.forName(config.getInitParameter("jdbcDriver"));
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}
	}

	protected void addItem(HttpServletRequest request, DataManager dm) {
		HttpSession session = request.getSession(true);
		Hashtable<String, CartItem> shoppingCart = (Hashtable<String, CartItem>) session.getAttribute("carrito");
		if (shoppingCart == null) {
			shoppingCart = new Hashtable<String, CartItem>(10);
		}

		try {
			String bookId = request.getParameter("bookId");
			Book book = dm.getBookDetails(bookId);
			if (book != null) {
				CartItem item = new CartItem(book, 1);
				shoppingCart.remove(bookId);
				shoppingCart.put(bookId, item);
				
			}
		} catch (Exception e) {
			System.out.println("Error adding the selected book to the shopping cart!");
		}

	}

	protected void updateItem(HttpServletRequest request, DataManager dm) {
		HttpSession session = request.getSession(true);
		Hashtable<String, CartItem> shoppingCart = (Hashtable<String, CartItem>) session.getAttribute("carrito");

		try {
			String bookId = request.getParameter("bookId");
			String quantity = request.getParameter("quantity");
			// controlamos que no hayan valores negativos en la cantidad de libros a añadir al pedido
			if (Integer.parseInt(quantity) < 0) {
				quantity = "0";
			}
			CartItem item = shoppingCart.get(bookId);
			if (item != null) {
				item.setQuantity(quantity);
			}
		} catch (Exception e) {
			System.out.println("Error updating shopping cart!");
		}
	}

	protected void deleteItem(HttpServletRequest request, DataManager dm) {
		HttpSession session = request.getSession(true);
		Hashtable<String, CartItem> shoppingCart = (Hashtable<String, CartItem>) session.getAttribute("carrito");

		try {
			String bookId = request.getParameter("bookId");
			shoppingCart.remove(bookId);
		} catch (Exception e) {
			System.out.println("Error deleting the selected item from the shopping cart!");
		}
	}

	// objetivo 5. Implementar el método validaLogin.
	// Puedes intentarlo SIN la base de datos (si no te sale)
	// este objetivo necesita de cambios en el switch/case

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		Hashtable<String, CartItem> shoppingCart = (Hashtable<String, CartItem>) sesion.getAttribute("carrito");
		if (shoppingCart == null) {
			shoppingCart = new Hashtable<String, CartItem>(10);
			sesion.setAttribute("carrito", shoppingCart);
		}
		String base = "/jsp/";
		String url = base + "index.jsp";
		String action = request.getParameter("action");
		// recuperar datamanager del contexto
		DataManager datamanager = (DataManager) request.getServletContext().getAttribute("dataManager");
		System.out.println("action: " + action);
		if (action != null) {
			switch (action) {
			case "showCart":
				url = base + "ShoppingCart.jsp";
				break;
			case "search":
				url = base + "SearchOutcome.jsp";
				break;
			case "selectCatalog":
				url = base + "SelectCatalog.jsp";
				break;
			case "bookDetails":

				url = base + "BookDetails.jsp";
				break;
			case "checkOut":
				url = base + "Checkout.jsp";
				break;
			case "orderConfirmation":
				boolean validado = validateCreditCard(request);
				if (validado) {url = base + "OrderConfirmation.jsp";} else {url = base + "Checkout.jsp";}
				break;
			case "addItem":
				addItem(request, datamanager);
				url = base + "ShoppingCart.jsp";
				break;
			case "updateItem":
				updateItem(request, datamanager);
				url = base + "ShoppingCart.jsp";
				break;
			case "deleteItem":
				deleteItem(request, datamanager);
				url = base + "ShoppingCart.jsp";
				break;
			case "login":
				String ruta = checkLogin(request, datamanager);
				url = base + ruta;
				break;

			// objetivo 5. Controlar funcionalidad del login

			}
		}
		RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}
	
	private String checkLogin(HttpServletRequest request, DataManager datamanager) {
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		PreparedStatement ps;
		try {
			ps = datamanager.getConnection().prepareStatement("SELECT * FROM usuarios WHERE usuario = ? AND clave = ?");
			ps.setString(1, user);
			ps.setString(2, password);
			ResultSet result = ps.executeQuery();
			// si el resulset devuelve una coincidencia con el siguiente if lo validamos y redirigimos a la página que tiene
			// el menú lateral, en caso contrario abajo devolvemos a index.jsp que es donde está el login
			if (result.next()) {
				return "index2.jsp";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("error", "Usuario y/o clave erróneo");
		return "index.jsp";
	}

	// este métdo es muy mejorable, pero siendo un exámen no me puedo entretener a refacorizarlo
	public boolean validateCreditCard(HttpServletRequest request) {
		boolean validado = false;
		String creditCard = request.getParameter("ccNumber");
		String expiryDateCard = request.getParameter("ccExpiryDate");
		String[] expiryDateCardArray = expiryDateCard.split("/");
		
		try {
			int day = Integer.parseInt(expiryDateCardArray[0]);
			int month = Integer.parseInt(expiryDateCardArray[1]);
			if ((day >= 1 && day <= 30) && (month >= 1 && month <= 12)) {
				validado = true;
			} else {
				request.setAttribute("error", "La tarjeta de crédito tiene la fecha de caducidad errónea");
				return false;
			}
		} catch (NumberFormatException e) {
			request.setAttribute("error", "La tarjeta de crédito tiene la fecha de caducidad errónea");
			return false;
		}
		
		if (creditCard.length() != 10) {
			validado = false;
			request.setAttribute("error", "La tarjeta de crédito debe contener 10 dígitos");
		} else {
			try {
				int creditCardNumber = Integer.parseInt(creditCard);
				validado = true;
			} catch (NumberFormatException e) {
				request.setAttribute("error", "La tarjeta de crédito debe contener 10 dígitos numéricos");
			}
			
		}
		return validado;
		
	}
	
	// lo dejo a medias no me dio tiempo
	 public List authenticate(String user, String password, DataManager dataManager) throws Exception {
		 PreparedStatement ps = dataManager.getConnection().prepareStatement("SELECT * FROM Cliente WHERE LOGIN_USUARIO = ? AND LOGIN_CLAVE = ?");
		 ps.setString(1, user);
		 ps.setString(2, password);
		 
		 
	        return null;
	 }
}
