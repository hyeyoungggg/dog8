package dev.mvc.users;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.dog.DogProcInter;
import dev.mvc.dog.DogVOMenu;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/users")
@Controller
public class UsersCont {
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc") // @Service("dev.mvc.member.MemberProc")
  private UsersProcInter usersProc;

  @Autowired
  @Qualifier("dev.mvc.dog.DogProc")
  private DogProcInter dogProc;

  public UsersCont() {
    System.out.println("-> UsersCont created.");
  }

  @GetMapping(value = "/checkID") // http://localhost:9091/useres/checkID?id=admin
  @ResponseBody
  public String checkID(@RequestParam(name = "id", defaultValue = "") String id) {
    System.out.println("-> id: " + id);
    int cnt = this.usersProc.checkID(id);

    // return "cnt: " + cnt;
    // return "{\"cnt\": " + cnt + "}"; // {"cnt": 1} JSON

    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();
  }

  /**
   * 회원 가입 폼
   *
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value = "/create") // http://localhost:9091/users/create
  public String create_form(Model model, @ModelAttribute("UsersVO") UsersVO usersVO) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return "users/create"; // /template/member/create.html
  }

  @PostMapping(value = "/create")
  public String create_proc(Model model, @ModelAttribute("usersVO") UsersVO usersVO) {
    int checkID_cnt = this.usersProc.checkID(usersVO.getId()); // 아이디 체크

    if (checkID_cnt == 0) {
      usersVO.setUsersgrade(15); // 기본 회원 15
      int cnt = this.usersProc.create(usersVO); // 회원가입

      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("usersname", usersVO.getUsersname());
        model.addAttribute("id", usersVO.getId());
      } else {
        model.addAttribute("code", "create_fail");
      }

      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }

    return "users/msg"; // /templates/users/msg.html
  }

  @GetMapping(value = "/list")
  public String list(HttpSession session, Model model) {


    if (this.usersProc.isAdmin(session)) {
      ArrayList<UsersVO> list = this.usersProc.list();

      model.addAttribute("list", list);

      return "users/list"; // /templates/users/list.html
    } else {
      return "redirect:/users/login_cookie_need?url=/users/list"; // redirect
    }

  }

//  /**
//   * 조회
//   *
//   * @param model
//   * @param memberno 회원 번호
//   * @return 회원 정보
//   */
//  @GetMapping(value = "/read")
//  public String read(Model model, @RequestParam(name = "usersno", defaultValue = "") int usersno) {
//    System.out.println("-> read usersno: " + usersno);
//
//    UsersVO usersVO = this.usersProc.read(usersno);
//    model.addAttribute("usersVO", usersVO);
//
//    ArrayList<DogVOMenu> menu = this.dogProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "users/read"; // templates/users/read.html
//  }

  /**
   * 조회
   * @param model
   * @param usersno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/read")
  public String read(HttpSession session,
                            Model model,
                            @RequestParam(name="usersno", defaultValue = "") int usersno) {
    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 사용자: 11 ~ 20
    // int gradeno = this.memberProc.read(usersno).getGrade(); // 등급 번호
    String usersgrade = (String)session.getAttribute("usersgrade"); // 등급: admin, member, guest
    Integer sessionUsersno = (Integer)session.getAttribute("usersno");

    // 사용자: member && 11 ~ 20
    // if (grade.equals("member") && (gradeno >= 11 && gradeno <= 20) && memberno == (int)session.getAttribute("memberno")) {
    if ("users".equals(usersgrade) &&  usersno == (int)session.getAttribute("usersno")) {
      System.out.println("-> read usersno: " + usersno);

      UsersVO usersVO = this.usersProc.read(usersno);
      model.addAttribute("usersVO", usersVO);

      return "users/read";  // templates/users/read.html

    } else if ("admin".equals(usersgrade)) {
      System.out.println("-> read usersno: " + usersno);

      UsersVO usersVO = this.usersProc.read(usersno);
      model.addAttribute("usersVO", usersVO);

      return "users/read";  // templates/member/read.html
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }

  }

  /**
   * 수정 처리
   *
   * @param model
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/update")
  public String update_proc(Model model, @ModelAttribute("useresVO") UsersVO usersVO) {
    int cnt = this.usersProc.update(usersVO); // 수정

    if (cnt == 1) {
      model.addAttribute("code", "update_success");
      model.addAttribute("usersname", usersVO.getUsersname());
      model.addAttribute("id", usersVO.getId());
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    return "users/msg"; // /templates/users/msg.html
  }

  /**
   * 삭제
   *
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/delete")
  public String delete(Model model, @RequestParam(name = "usersno", defaultValue = "") int usersno) {
    System.out.println("-> delete usersno: " + usersno);

    UsersVO usersVO = this.usersProc.read(usersno);
    model.addAttribute("usersVO", usersVO);

    return "users/delete"; // templates/users/delete.html
  }

  /**
   * 회원 Delete process
   *
   * @param model
   * @param memberno 삭제할 레코드 번호
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_process(Model model, @RequestParam(name = "usersno", defaultValue = "") Integer usersno) {
    int cnt = this.usersProc.delete(usersno);

    if (cnt == 1) {
      return "redirect:/users/list"; // @GetMapping(value="/list"), list를 다시 요청하는
    } else {
      model.addAttribute("code", "delete_fail");
      return "users/msg"; // /templates/users/msg.html
    }
  }

//----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 시작
  // ----------------------------------------------------------------------------------
  /**
   * 로그인
   *
   * @param model
   * @param usersno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/login")
  public String login_form(Model model, HttpServletRequest request, HttpSession session) {
    System.out.println("-> 시스템 session.id: " + session.getId());
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크

    if (cookies != null) { // 쿠키가 존재한다면
      for (Cookie element : cookies) {
        cookie = element; // 쿠키 객체 추출

        if (cookie.getName().equals("ck_id")) {
          ck_id = cookie.getValue(); // email
        } else if (cookie.getName().equals("ck_id_save")) {
          ck_id_save = cookie.getValue(); // Y, N
        } else if (cookie.getName().equals("ck_passwd")) {
          ck_passwd = cookie.getValue(); // 1234
        } else if (cookie.getName().equals("ck_passwd_save")) {
          ck_passwd_save = cookie.getValue(); // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------

    // <input type='text' class="form-control" name='id' id='id'
    // th:value='${ck_id }' required="required"
    // style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);

    // <input type='checkbox' name='id_save' value='Y'
    // th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);

    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//   model.addAttribute("ck_id_save", "Y");
//   model.addAttribute("ck_passwd_save", "Y");

    return "users/login_cookie"; // templates/member/login_cookie.html
  }

//  /**
//   * Cookie 기반 로그인 처리
//   *
//   * @param session
//   * @param request
//   * @param response
//   * @param model
//   * @param id          아이디
//   * @param passwd      패스워드
//   * @param id_save     아이디 저장 여부
//   * @param passwd_save 패스워드 저장 여부
//   * @return
//   */
//  @PostMapping(value = "/login")
//  public String login_proc(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
//      @RequestParam(value = "id", defaultValue = "") String id,
//      @RequestParam(value = "userspasswd", defaultValue = "") String userspasswd,
//      @RequestParam(value = "id_save", defaultValue = "") String id_save,
//      @RequestParam(value = "passwd_save", defaultValue = "") String passwd_save) {
//    HashMap<String, Object> map = new HashMap<String, Object>();
//    map.put("id", id);
//    map.put("userspasswd", userspasswd);
//
//    int cnt = this.usersProc.login(map);
//    System.out.println("-> login_proc cnt: " + cnt);
//
//    model.addAttribute("cnt", cnt);
//
//    if (cnt == 1) {
//      // id를 이용하여 회원 정보 조회
//      UsersVO usersVO = this.usersProc.readById(id);
//      session.setAttribute("usersno", usersVO.getUsersno());
//      // session.setAttribute("id", memverVO.getId()); // 시스템 변수와 중복됨 권장하지 않음
//      session.setAttribute("id", usersVO.getId());
//      session.setAttribute("usersname", usersVO.getUsersname());
//      session.setAttribute("usersgrade", usersVO.getUsersgrade());
//
//      // Cookie 관련 코드---------------------------------------------------------
//      // -------------------------------------------------------------------
//      // id 관련 쿠기 저장
//      // -------------------------------------------------------------------
//      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
//        Cookie ck_id = new Cookie("ck_id", id);
//        ck_id.setPath("/"); // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
//        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
//        response.addCookie(ck_id); // id 저장
//      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
//        Cookie ck_id = new Cookie("ck_id", "");
//        ck_id.setPath("/");
//        ck_id.setMaxAge(0);
//        response.addCookie(ck_id); // id 저장
//      }
//
//      // id를 저장할지 선택하는 CheckBox 체크 여부
//      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
//      ck_id_save.setPath("/");
//      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
//      response.addCookie(ck_id_save);
//      // -------------------------------------------------------------------
//
//      // -------------------------------------------------------------------
//      // Password 관련 쿠기 저장
//      // -------------------------------------------------------------------
//      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
//        Cookie ck_passwd = new Cookie("ck_passwd", userspasswd);
//        ck_passwd.setPath("/");
//        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
//        response.addCookie(ck_passwd);
//      } else { // N, 패스워드를 저장하지 않을 경우
//        Cookie ck_passwd = new Cookie("ck_passwd", "");
//        ck_passwd.setPath("/");
//        ck_passwd.setMaxAge(0);
//        response.addCookie(ck_passwd);
//      }
//      // passwd를 저장할지 선택하는 CheckBox 체크 여부
//      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
//      ck_passwd_save.setPath("/");
//      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
//      response.addCookie(ck_passwd_save);
//      // -------------------------------------------------------------------
//      // ----------------------------------------------------------------------------
//
//      return "redirect:/";
//    } else {
//      model.addAttribute("code", "login_fail");
//      return "users/msg";
//    }
//  }

  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------

//  /**
//   * 패스워드 수정 폼
//   *
//   * @param model
//   * @param useresno
//   * @return
//   */
//  @GetMapping(value = "/passwd_update")
//  public String passwd_update_form(HttpSession session, Model model) {
//    int usersno = (int) session.getAttribute("usersno"); // session에서 가져오기
//
//    UsersVO usersVO = this.usersProc.read(usersno);
//
//    model.addAttribute("usersVO", usersVO);
//
//    return "users/passwd_update";
//  }

  /**
   * 현재 패스워드 확인
   *
   * @param session
   * @param current_passwd
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value = "/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    JSONObject src = new JSONObject(json_src); // String -> JSON
    String current_passwd = (String) src.get("current_passwd"); // 값 가져오기
    System.out.println("-> current_passwd: " + current_passwd);

    try {
      Thread.sleep(3000);
    } catch (Exception e) {

    }

    int usersno = (int) session.getAttribute("usersno"); // session에서 가져오기
    HashMap<String, Object> map = new HashMap<>();
    map.put("usersno", usersno);
    map.put("userspasswd", current_passwd);

    int cnt = this.usersProc.passwd_check(map);

    JSONObject json = new JSONObject();
    json.put("cnt", cnt); // 1일치
    System.out.println(json.toString());

    return json.toString();
  }
//
//  /**
//   * 패스워드 변경
//   *
//   * @param session
//   * @param model
//   * @param current_passwd 현재 패스워드
//   * @param passwd         새로운 패스워드
//   * @return
//   */
//  @PostMapping(value = "/passwd_update_proc")
//  public String update_passwd_proc(HttpSession session, Model model,
//      @RequestParam(value = "current_passwd", defaultValue = "") String current_passwd,
//      @RequestParam(value = "userspasswd", defaultValue = "") String userspasswd) {
//    int usersno = (int) session.getAttribute("usersno"); // session에서 가져오기
//    HashMap<String, Object> map = new HashMap<String, Object>();
//    map.put("usersno", usersno);
//    map.put("userspasswd", current_passwd);
//
//    int cnt = this.usersProc.passwd_check(map);
//
//    if (cnt == 0) { // 패스워드 불일치
//      model.addAttribute("code", "userspasswd_not_equal");
//      model.addAttribute("cnt", 0);
//
//    } else { // 패스워드 일치
//      map = new HashMap<String, Object>();
//      map.put("usersno", usersno);
//      map.put("userspasswd", userspasswd); // 새로운 패스워드
//
//      int passwd_change_cnt = this.usersProc.passwd_update(map);
//
//      if (passwd_change_cnt == 1) {
//        model.addAttribute("code", "userspasswd_change_success");
//        model.addAttribute("cnt", 1);
//      } else {
//        model.addAttribute("code", "userspasswd_change_fail");
//        model.addAttribute("cnt", 0);
//      }
//    }
//
//    return "users/msg"; // /templates/users/msg.html
//  }

  /**
   * 패스워드 수정 폼
   * @param model
   * @param usersno
   * @return
   */
  @GetMapping(value="/passwd_update")
  public String passwd_update_form(HttpSession session, Model model) {
    if (this.usersProc.isUsers(session)) {
      int usersno = (int)session.getAttribute("usersno"); // session에서 가져오기

      UsersVO usersVO = this.usersProc.read(usersno);

      model.addAttribute("usersVO", usersVO);

      return "users/passwd_update";    // /templates/member/passwd_update.html
    } else {
      return "redirect:/users/login_cookie_need"; // redirect
    }

  }

  /**
   * 패스워드 변경
   * @param session
   * @param model
   * @param current_passwd 현재 패스워드
   * @param passwd 새로운 패스워드
   * @return
   */
  @PostMapping(value="/passwd_update_proc")
  public String passwd_update_proc(HttpSession session,
                                                    Model model,
                                                    @RequestParam(value="current_passwd", defaultValue = "") String current_passwd,
                                                    @RequestParam(value="userspasswd", defaultValue = "") String userspasswd) {

    if (current_passwd == null || current_passwd.trim().length() == 0 ||
        userspasswd == null || userspasswd.trim().length() == 0) {
        model.addAttribute("code", "passwd_empty");
        model.addAttribute("cnt", 0);
        return "users/msg";
    }
    
    if (this.usersProc.isUsers(session)) {
      int usersno = (int)session.getAttribute("usersno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<>();
      map.put("usersno", usersno);
      map.put("userspasswd", current_passwd);

      int cnt = this.usersProc.passwd_check(map);

      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);

      } else { // 패스워드 일치
        map = new HashMap<>();
        map.put("usersno", usersno);
        map.put("userspasswd", userspasswd); // 새로운 패스워드

        int passwd_change_cnt = this.usersProc.passwd_update(map);

        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "users/msg";   // /templates/users/msg.html
    } else {
      return "redirect:/users/login_cookie_need"; // redirect
    }

  }

  /**
   * 로그아웃
   *
   * @param model
   * @param usersno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate(); // 모든 세션 변수 삭제
    return "redirect:/";
  }

  /**
   * Cookie 기반 로그인 처리
   *
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id          아이디
   * @param passwd      패스워드
   * @param id_save     아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
  @PostMapping(value = "/login")
  public String login_proc(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
                                @RequestParam(value = "id", defaultValue = "") String id,
                                @RequestParam(value = "userspasswd", defaultValue = "") String userspasswd,
                                @RequestParam(value = "id_save", defaultValue = "") String id_save,
                                @RequestParam(value = "passwd_save", defaultValue = "") String passwd_save,
                                @RequestParam(value = "url", defaultValue = "") String url
                                ) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("userspasswd", userspasswd);

    int cnt = this.usersProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);

    model.addAttribute("cnt", cnt);

    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      UsersVO usersVO = this.usersProc.readById(id);
      session.setAttribute("usersno", usersVO.getUsersno());
      session.setAttribute("id", usersVO.getId());
      session.setAttribute("usersname", usersVO.getUsersname());
      // session.setAttribute("usersgrade", usersVO.getUsersgrade());

      // -------------------------------------------------------------------
      // 회원 등급 처리
      // -------------------------------------------------------------------
      if (usersVO.getUsersgrade() >= 1 && usersVO.getUsersgrade() <= 10) {
        session.setAttribute("usersgrade", "admin");
      } else if (usersVO.getUsersgrade() >= 11 && usersVO.getUsersgrade() <= 20) {
        session.setAttribute("usersgrade", "users");
      } else if (usersVO.getUsersgrade() >= 21) {
        session.setAttribute("usersgrade", "guest");
      }
      
      System.out.println("-> login_proc grade: " + usersVO.getUsersgrade());

      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie ck_id = new Cookie("ck_id", id);
        ck_id.setPath("/"); // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(ck_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie ck_id = new Cookie("ck_id", "");
        ck_id.setPath("/");
        ck_id.setMaxAge(0); // 0초
        response.addCookie(ck_id); // id 저장
      }

      // id를 저장할지 선택하는 CheckBox 체크 여부
      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
      ck_id_save.setPath("/");
      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_id_save);
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // Password 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie ck_passwd = new Cookie("ck_passwd", userspasswd);
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(ck_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie ck_passwd = new Cookie("ck_passwd", "");
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(0);
        response.addCookie(ck_passwd);
      }
      // passwd를 저장할지 선택하는 CheckBox 체크 여부
      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
      ck_passwd_save.setPath("/");
      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------

      if (url.length() > 0) {
        return "redirect:" + url;
      } else {
      return "redirect:/";
      }
    } else {
      model.addAttribute("code", "login_fail");
      return "users/msg";
    }
  }

  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------

  /**
   * 로그인 요구에 따른 로그인 폼 출력
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login_cookie_need")
  public String login_cookie_need(Model model, HttpServletRequest request,
                                          @RequestParam(name="url", defaultValue = "") String url
                                          ) {
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크

    if (cookies != null) { // 쿠키가 존재한다면
      for (Cookie element : cookies) {
        cookie = element; // 쿠키 객체 추출

        if (cookie.getName().equals("ck_id")){
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------

    //    <input type='text' class="form-control" name='id' id='id'
    //            th:value='${ck_id }' required="required"
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);

    //    <input type='checkbox' name='id_save' value='Y'
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);

    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");

    model.addAttribute("url", url);

    return "users/login_cookie_need";  // templates/member/login_cookie_need.html
  }

}
