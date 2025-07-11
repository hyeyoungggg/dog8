package dev.mvc.users;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dog_tool.Security;
import jakarta.servlet.http.HttpSession;

@Component("dev.mvc.users.UsersProc")
public class UsersProc implements UsersProcInter {
  @Autowired
  private UsersDAOInter usersDAO;

  @Autowired
  Security security;

  public UsersProc(){
    // System.out.println("-> MemberProc created.");
  }

  @Override
  public int checkID(String id) {
    int cnt = this.usersDAO.checkID(id);
    return cnt;
  }

  @Override
  public ArrayList<UsersVO> list() {
    ArrayList<UsersVO> list = this.usersDAO.list();
    return list;
  }

  @Override
  public UsersVO read(int usersno) {
    UsersVO usersVO = this.usersDAO.read(usersno);
    return usersVO;
  }

  @Override
  public UsersVO readById(String id) {
    UsersVO usersVO = this.usersDAO.readById(id);
    return usersVO;
  }

//@Override
//  public boolean isUsers(HttpSession session){
//    boolean sw = false; // 로그인하지 않은 것으로 초기화
//    int ugrade = 99;
//
//    // System.out.println("-> grade: " + session.getAttribute("grade"));
//    if (session != null) {
//      String id = (String)session.getAttribute("id");
//      if (session.getAttribute("ugrade") != null) {
//        ugrade = (int)session.getAttribute("ugrade");
//      }
//
//      if (id != null && ugrade <= 20){ // 관리자 + 회원
//        sw = true;  // 로그인 한 경우
//      }
//    }
//
//    return sw;
//  }
//
//  @Override
//  public boolean isUsersAdmin(HttpSession session){
//    boolean sw = false; // 로그인하지 않은 것으로 초기화
//    int ugrade = 99;
//
//    // System.out.println("-> grade: " + session.getAttribute("grade"));
//    if (session != null) {
//      String id = (String)session.getAttribute("id");
//      if (session.getAttribute("ugrade") != null) {
//        ugrade = (int)session.getAttribute("ugrade");
//      }
//
//      if (id != null && ugrade <= 10){ // 관리자
//        sw = true;  // 로그인 한 경우
//      }
//    }
//
//    return sw;
//  }

  @Override
  public int update(UsersVO usersVO) {
    int cnt = this.usersDAO.update(usersVO);
    return cnt;
  }

  @Override
  public int delete(int usersno) {
    int cnt = this.usersDAO.delete(usersno);
    return cnt;
  }

  @Override
  public int create(UsersVO usersVO) {
    String userspasswd = usersVO.getUserspasswd();
    // Security security = new Security();
    String passwd_encoded = this.security.aesEncode(userspasswd);
    usersVO.setUserspasswd(passwd_encoded);

    // memberVO.setPasswd(new Security().aesEncode(memberVO.getPasswd())); // 단축형

    int cnt = this.usersDAO.create(usersVO);
    return cnt;
  }

  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String userspasswd = (String)map.get("userspasswd");
    userspasswd = this.security.aesEncode(userspasswd);
    map.put("userspasswd", userspasswd);

    int cnt = this.usersDAO.passwd_check(map);
    return cnt;
  }

  @Override
  public int passwd_update(HashMap<String, Object> map) {
    String userspasswd = (String)map.get("userspasswd");
    userspasswd = this.security.aesEncode(userspasswd);
    map.put("userspasswd", userspasswd);

    int cnt = this.usersDAO.passwd_update(map);
    return cnt;
  }

  @Override
  public int login(HashMap<String, Object> map) {
    String userspasswd = (String)map.get("userspasswd");
    userspasswd = this.security.aesEncode(userspasswd);
    map.put("userspasswd", userspasswd);

    int cnt = this.usersDAO.login(map);

    return cnt;
  }

  @Override
  public boolean isUsers(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화

    if (session.getAttribute("usersgrade") != null) {
      if (((String)session.getAttribute("usersgrade")).equals("admin") ||
          ((String)session.getAttribute("usersgrade")).equals("users")) {
        sw = true;
      }
    }

    return sw;
  }

  @Override
  public boolean isAdmin(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화

    if (session.getAttribute("usersgrade") != null) {
      if (((String)session.getAttribute("usersgrade")).equals("admin")) {
        sw = true;
      }
    }

    return sw;
  }

}