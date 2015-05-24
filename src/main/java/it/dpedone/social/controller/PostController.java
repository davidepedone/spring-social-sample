package it.dpedone.social.controller;

import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController {
	
	@RequestMapping(value="/updatestatus",method=RequestMethod.GET)
	public String updateStatus(){
		try{			
			
			PagePostData pagePost = new PagePostData("pageid");
			pagePost.message("Test new message");
			
			FacebookTemplate fb = new FacebookTemplate("accesstoken");
			fb.pageOperations().post(pagePost);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return "update";
	}
}
