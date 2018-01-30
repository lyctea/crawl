package com.hehe.crawl.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//@Controller
public class FirstController {
    
    private String name;
    
    private String password;
    
  /*  @Autowired
    CitycodeEntityMapper mapper;*/
    
    @RequestMapping("/")
    @ResponseBody
    String home()
    {
        return "Hello Springboot!";
    }
    
    @RequestMapping("/hello")
    @ResponseBody
    String hello()
    {
    	/*CitycodeEntityExample example = new CitycodeEntityExample();
    	example.createCriteria().andIdEqualTo(335);
		List<CitycodeEntity> list = mapper.selectByExample(example );*/
        return "name: " + name + ", " + "password: " + password;
    }
}