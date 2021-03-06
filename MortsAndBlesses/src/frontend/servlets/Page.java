package frontend.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.coyote.Request;

import backend.routes.BackEndRoutes;
import frontend.modele.module.User;
import frontend.tools.HttpUtility;
import frontend.tools.TokenParse;

/**
 * Servlet implementation class Page
 */
@WebServlet(urlPatterns= {"/Home","/Profil","/Deconnecte","/Information"})
public class Page extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (request.getServletPath().toLowerCase().equals(routes.deconnecte)) {
			HttpServletRequest request1 = (HttpServletRequest) request;
			HttpSession session = request1.getSession();
			System.out.println("session: " + session.getAttribute("token"));
			if (session.getAttribute("token") != null) {
				session.removeAttribute("token");
			}
			else
				response.sendRedirect("/MortsAndBlesses/Login");

		}

		
		
		if (request.getServletPath().toLowerCase().equals(routes.home)) {
			HttpServletRequest request1 = (HttpServletRequest) request;
			HttpSession session = request1.getSession();
			System.out.println("session: " + session.getAttribute("token"));
			if (session.getAttribute("token") != null)
				request.getRequestDispatcher("/Pages/Home.jsp").forward(request, response);
			else
				response.sendRedirect("/MortsAndBlesses/Login");

		}

		if (request.getServletPath().toLowerCase().equals(routes.profil)) {
			HttpServletRequest request1 = (HttpServletRequest) request;
			HttpSession session = request1.getSession();
			System.out.println("session: " + session.getAttribute("token"));
			if (session.getAttribute("token") != null) {
				User user =TokenParse.parse((String)session.getAttribute("token"));
				request.setAttribute("user", user);
				request.getRequestDispatcher("/Pages/profil.jsp").forward(request, response);
			}
			else
				response.sendRedirect("/MortsAndBlesses/Login");

		}
		

		if (request.getServletPath().toLowerCase().equals(routes.mise_ajour)) {
			HttpServletRequest request1 = (HttpServletRequest) request;
			HttpSession session = request1.getSession();
			System.out.println("session: " + session.getAttribute("token"));
			if (session.getAttribute("token") != null) {
				User user =TokenParse.parse((String)session.getAttribute("token"));
				request.setAttribute("user", user);
				request.getRequestDispatcher("/Pages/modifierInfo.jsp").forward(request, response);
			}
			else
				response.sendRedirect("/MortsAndBlesses/Login");

		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getServletPath().toLowerCase().equals(routes.mise_ajour)) {
			System.out.println("post update");
			if (request.getParameter("username") != null && request.getParameter("password") != null
					&& request.getParameter("nom") != null && request.getParameter("prenom") != null
					&& request.getParameter("email") != null && request.getParameter("date_de_naissance") != null) {
				System.out.println("hnaa");
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("username", request.getParameter("username"));
				params.put("password", request.getParameter("password"));
				params.put("nom", request.getParameter("nom"));
				params.put("date_de_naissance", request.getParameter("date_de_naissance"));
				params.put("email", request.getParameter("email"));
				params.put("prenom", request.getParameter("prenom"));

				HttpUtility.newRequest(request, response, BackEndRoutes.server+BackEndRoutes.users_edit, HttpUtility.METHOD_POST, params,
						new HttpUtility.Callback() {

							@Override
							public void OnSuccess(String respons) {
								try {
									System.out.println("Server OnSuccess response=" + respons);
									response.sendRedirect("/MortsAndBlesses/Login");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

							@Override
							public void OnError(int status_code, String message) {
								try {
									System.out.println("Server OnError status_code=" + status_code + " message=" + message);
									request.getRequestDispatcher("/Pages/singup.jsp").forward(request, response);
								} catch (ServletException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});

			}
		}
	}

}
