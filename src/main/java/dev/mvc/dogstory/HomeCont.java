package dev.mvc.dogstory;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.dog.DogProcInter;
import dev.mvc.dog.DogVOMenu;
import dev.mvc.dog_tool.Security;

@Controller
public class HomeCont {
  @Autowired
  @Qualifier("dev.mvc.dog.DogProc")
  private DogProcInter dogProc;

  @Autowired
  private Security security;

  public HomeCont() {
    System.out.println("-> HomeCont created.");
  }

  @GetMapping(value="/") // http://localhost:9091
  public String home(Model model) { // 파일명 return
    if (this.security != null) {
      System.out.println("-> 객체 고유 코드: " + security.hashCode());
      System.out.println(security.aesEncode("1234"));
    }

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return "index"; // /templates/index.html
  }

}
