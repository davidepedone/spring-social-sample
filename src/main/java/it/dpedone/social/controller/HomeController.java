package it.dpedone.social.controller;

import javax.inject.Inject;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {
    private Facebook facebook;

    @Inject
    public HomeController(Facebook facebook) {
        this.facebook = facebook;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloFacebook(Model model) {
    	try{
            if (!facebook.isAuthorized()) {
                return "redirect:/connect/facebook";
            }
            model.addAttribute(facebook.userOperations().getUserProfile());
            PagedList<Post> homeFeed = facebook.feedOperations().getHomeFeed();
            model.addAttribute("feed", homeFeed);

            return "home";
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    		return "redirect:/connect/facebook";
    	}
    }
}
