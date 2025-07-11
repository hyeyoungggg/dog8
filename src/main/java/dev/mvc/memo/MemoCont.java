package dev.mvc.memo;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.mvc.dog.DogProcInter;
import dev.mvc.dog.DogVOMenu;
import dev.mvc.users.UsersProcInter;

@Controller
@RequestMapping("/memo")
public class MemoCont {

  @Autowired
  @Qualifier("dev.mvc.memo.memoProc")
  private MemoProcInter memoProc;
  
  @Autowired
  @Qualifier("dev.mvc.dog.DogProc") // @Component("dev.mvc.cate.CateProc")
  private DogProcInter dogProc;


  @Autowired
  @Qualifier("dev.mvc.users.UsersProc")
  private UsersProcInter usersProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 , @GetMapping(value="/list_search") */
  private String list_url = "/dog/list_search";

  /** 메모 등록 폼 */
  @GetMapping("/create")
  public String createForm(HttpSession session, Model model) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }
    return "memo/create";
  }

  /** 메모 등록 처리 */
  @PostMapping("/create")
  public String createSubmit(HttpSession session, MemoVO memoVO, Model model) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    int usersno = (int) session.getAttribute("usersno");
    memoVO.setUsersno(usersno);

    int cnt = memoProc.create(memoVO);
    if (cnt == 1) {
      model.addAttribute("code", "create_success");
      model.addAttribute("title", memoVO.getTitle());

//      // 3번 기능: 방금 등록한 메모 상세로 이동
//      MemoVO recentMemo = memoProc.read(memoVO.getMemono());
//      model.addAttribute("memoVO", recentMemo);
      return "redirect:/memo/list_all";
    } else {
      model.addAttribute("code", "create_fail");
      model.addAttribute("title", memoVO.getTitle());
      model.addAttribute("cnt", cnt);
      return "memo/msg";
    }
  }

  /** 메모 목록 */
  @GetMapping("/list_all")
  public String list_all(@RequestParam(name = "sort", defaultValue = "latest") String sort, 
                         HttpSession session, Model model) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    List<MemoVO> list;

    if ("viewcnt".equals(sort)) {
      list = memoProc.list_by_viewcnt();
    } else {
      list = memoProc.list(); // 기본값: 최신순
    }

    model.addAttribute("list", list);
    model.addAttribute("sort", sort);

    return "memo/list_all";
  }

  /** 메모 읽기 */
  @GetMapping("/read")
  public String read(HttpSession session, Model model,
      @RequestParam(value = "memono", required = false) Integer memono) {

    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    if (memono == null) {
      return "redirect:/memo/list_all";
    }

    // ⭐ 조회수 증가
    memoProc.increaseViewcnt(memono);

    MemoVO memoVO = memoProc.read(memono);
    model.addAttribute("memoVO", memoVO);
    
 // 이전/다음 메모
    MemoVO prevMemo = memoProc.prev(memono);
    MemoVO nextMemo = memoProc.next(memono);
    
    model.addAttribute("prevMemo", prevMemo);
    model.addAttribute("nextMemo", nextMemo);

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return "memo/read";
  }

  /** 메모 수정 폼 */
  @GetMapping("/update")
  public String updateForm(HttpSession session, Model model, @RequestParam("memono") int memono) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    MemoVO memoVO = memoProc.read(memono);
    model.addAttribute("memoVO", memoVO);
    
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return "memo/update";
  }

  /** 메모 수정 처리 */
  @PostMapping("/update")
  public String updateSubmit(HttpSession session, MemoVO memoVO) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    memoProc.update(memoVO);
    return "redirect:/memo/list_all";
  }

  /** 메모 삭제 */
  @GetMapping("/delete")
  public String delete(Model model, HttpSession session, @RequestParam("memono") int memono) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);
    
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    MemoVO memoVO = memoProc.read(memono); // ← 이게 null이 아닌지 확인
    model.addAttribute("memoVO", memoVO); // ← 반드시 있어야 HTML에서 th:text 가능
    return "memo/delete";
  }
  
  /** 메모 수정 처리 */
  @PostMapping("/delete")
  public String deleteSubmit(HttpSession session, @RequestParam("memono") int memono) {
    if (!usersProc.isAdmin(session)) {
      return "redirect:/users/login_cookie_need";
    }

    int cnt = memoProc.delete(memono);
    return "redirect:/memo/list_all";
  }

}
