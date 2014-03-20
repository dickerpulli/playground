package de.tbosch.web.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EchoController {

	@RequestMapping(value = "/echo/{value}", method = RequestMethod.GET)
	@ResponseBody
	public String echo(@PathVariable String value) {
		return value;
	}

	@RequestMapping(value = "/echopage/{value}", method = RequestMethod.GET)
	public String echopage(@PathVariable String value, ModelAndView modelAndView) {
		modelAndView.addObject("value", value);
		return "echo";
	}

}
