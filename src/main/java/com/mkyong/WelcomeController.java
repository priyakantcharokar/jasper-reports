package com.mkyong;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class WelcomeController {
	
	@Autowired
	private UserService userService;

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("message", this.message);
		return "welcome";
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public void export(ModelAndView model, HttpServletResponse response) throws IOException, JRException, SQLException {
		JasperPrint jasperPrint = null;

		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"users.pdf\""));

		OutputStream out = response.getOutputStream();
		jasperPrint = userService.exportPdfFile();
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
	}

}