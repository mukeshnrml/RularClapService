package com.smart.controller;

import javax.servlet.http.HttpSession;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.Employer;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.services.EmployerService;



@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "Home - Rural Clap");
		return "index";
	}

	
	
	
	

    @Autowired
    private EmployerService employerService;

    // GET request for registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employer", new Employer());
        return "register";
    }

    // POST request to handle registration
    @PostMapping("/register")
    public String registerEmployer(@ModelAttribute("employer") Employer employer, Model model) {
        employerService.registerEmployer(employer);
        model.addAttribute("message", "Registration successful!");
        return "employerlogin";  // Redirect to login page after successful registration
    }

    // GET request for login form
    @GetMapping("/employerlogin")
    public String showLoginForm(Model model) {
        model.addAttribute("employer", new Employer());
        return "employerlogin";
    }

    // POST request to handle login
    @PostMapping("/employerlogin")
    public String loginEmployer(@ModelAttribute("employer") Employer employer, Model model, HttpSession session) {
        Employer loggedInEmployer = employerService.loginEmployer(employer.getEmail(), employer.getPassword());
        if (loggedInEmployer != null) {
            session.setAttribute("employerSession", loggedInEmployer); // Store employer in session
            return "redirect:/dashboard";  // Redirect to dashboard upon successful login
        }
        model.addAttribute("error", "Invalid credentials!");
        return "employerlogin";
    }

    // GET request for employer dashboard
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Employer employer = (Employer) session.getAttribute("employerSession");
        if (employer == null) {
            return "redirect:/employerlogin"; // If no employer is logged in, redirect to login page
        }
        model.addAttribute("employer", employer);
        
        List<Contact> contacts = employerService.getAllContacts();
        model.addAttribute("contacts", contacts);
        return "dashboard";  // Render dashboard view
    }

    // GET request for logout
    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus status, Model model) {
        session.invalidate();  // Invalidate the session
        status.setComplete();  // Mark the session as complete
        model.addAttribute("message", "Logout successful!");
        return "index";  // Redirect to index page with logout success message
    }

       
    
    
	
	
	
	
	@GetMapping("services.html")
	public String service() {
		return "services";
	}
	
	
	

	
	
	@GetMapping("testimonial.html")
	public String testimonial() {
		return "testimonial";
	}
	
	@RequestMapping("contact.html")
	public String contact() {
		return "contact";
	}
	@RequestMapping("index.html")
	public String home() {
		return "index";
	}
	@RequestMapping("team.html")
	public String team() {
		return "team";
	}
	
	@RequestMapping("about.html")
	public String about() {
		return "about";
	}
	

	
	

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register -  RularClap");
		model.addAttribute("user", new User());
		return "signup";
	}

	// handler for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}

			if (result1.hasErrors()) {
				System.out.println("ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			System.out.println("Agreement " + agreement);
			System.out.println("USER " + user);

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());

			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}
}
