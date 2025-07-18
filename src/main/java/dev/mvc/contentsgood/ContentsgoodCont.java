package dev.mvc.contentsgood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.dcontents.DcontentsProcInter;
import dev.mvc.dog.DogProcInter;
import dev.mvc.dog.DogVOMenu;
import dev.mvc.users.UsersProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/contentsgood")
public class ContentsgoodCont {
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc") // @Service("dev.mvc.users.UsersProc")
  private UsersProcInter usersProc;
  
  @Autowired
  @Qualifier("dev.mvc.dog.DogProc") // @Component("dev.mvc.dog.DogProc")
  private DogProcInter dogProc;
  
  @Autowired
  @Qualifier("dev.mvc.dcontents.DcontentsProc") 
  private DcontentsProcInter dcontentsProc;
  
  @Autowired
  @Qualifier("dev.mvc.contentsgood.ContentsgoodProc") 
  private ContentsgoodProcInter contentsgoodProc;
  
  public ContentsgoodCont() {
    System.out.println("-> ContentsgoodCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody ContentsgoodVO contentsgoodVO) {
    System.out.println("-> 수신 데이터:" + contentsgoodVO.toString());
    
//    int usersno = 1; // test 
    int usersno = (int)session.getAttribute("usersno"); // 보안성 향상
    contentsgoodVO.setUsersno(usersno);
    
    int cnt = this.contentsgoodProc.create(contentsgoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
//  /**
//   * 목록
//   * 
//   * @param model
//   * @return
//   */
//  // http://localhost:9091/contentsgood/list_all
//  @GetMapping(value = "/list_all")
//  public String list_all(Model model) {
//    ArrayList<ContentsgoodVO> list = this.contentsgoodProc.list_all();
//    model.addAttribute("list", list);
//
//    ArrayList<DogVOMenu> menu = this.dogProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "contentsgood/list_all"; // /templates/contentsgood/list_all.html
//  }
  
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/contentsgood/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<ContentsContentsgoodUsersnoVO> list = this.contentsgoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return "contentsgood/list_all_join"; // /templates/contentsgood/list_all_join.html
  }
  
  /**
   * 삭제 처리 http://localhost:9091/contentsgood/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="contentsgoodno", defaultValue = "0") int contentsgoodno, 
      RedirectAttributes ra) {    
    
    if (this.usersProc.isAdmin(session)) { // 관리자 로그인 확인
      ContentsgoodVO vo = this.contentsgoodProc.read(contentsgoodno);
      int dogcontno = vo.getDogcontno();
      this.contentsgoodProc.delete(contentsgoodno);       // 삭제
      this.dcontentsProc.decreaseRecom(dogcontno);

      return "redirect:/contentsgood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/users/login_cookie_need"); // /templates/users/login_cookie_need.html
      return "redirect:/contentsgood/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
}







