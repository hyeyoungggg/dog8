package dev.mvc.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import dev.mvc.reply.ReplyProcInter;
import dev.mvc.reply.ReplyVO;
import dev.mvc.replylike.ReplyLikeProcInter;
import dev.mvc.replylike.ReplyLikeVO;
import dev.mvc.users.UsersProcInter;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/reply")
@Controller
public class ReplyCont {
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProcInter replyProc;
  
  @Autowired
  @Qualifier("dev.mvc.replylike.ReplyLikeProc")
  private ReplyLikeProcInter replyLikeProc;
  
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc")
  private UsersProcInter usersProc;

  @GetMapping(value = "/list_by_dogcontno")
  public String list_by_dogcontno(Model model, 
                                   HttpSession session,
                                   @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno) {

    List<ReplyVO> list = this.replyProc.list_by_dogcontno(dogcontno);

    // 로그인 사용자 번호 가져오기
    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) {
      usersno = 0; // 비회원일 경우 0으로 처리
    }

    // 각 댓글에 좋아요 수와 현재 사용자의 좋아요 여부 설정
    for (ReplyVO reply : list) {
      int likeCount = replyLikeProc.countByReplyno(reply.getReplyno());
      boolean isLiked = replyLikeProc.isLiked(Map.of("replyno", reply.getReplyno(), "usersno", usersno)) == 1;

      reply.setLikeCount(likeCount);
      reply.setLiked(isLiked);
    }

    model.addAttribute("replyList", list);
    model.addAttribute("dogcontno", dogcontno);

    return "reply/list_all";  // /templates/reply/list_all.html
  }


  /**
   * 댓글 등록
   */
  @PostMapping(value = "/create")
  public String create(HttpSession session, Model model, 
                       @ModelAttribute("replyVO") ReplyVO replyVO) {
    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) {
      model.addAttribute("msg", "로그인이 필요합니다.");
      model.addAttribute("url", "/users/login");
      return "reply/msg";
    }

    replyVO.setUsersno(usersno); // ★★★ 반드시 필요
    
    int cnt = this.replyProc.create(replyVO);

    if (cnt == 1) {
      this.replyProc.increaseReplycnt(replyVO.getDogcontno());
      model.addAttribute("msg", "댓글이 등록되었습니다.");
    } else {
      model.addAttribute("msg", "댓글 등록에 실패했습니다.");
    }

    model.addAttribute("url", "/dcontents/read?dogcontno=" + replyVO.getDogcontno());
    return "reply/msg";
  }

  /**
   * 댓글 수정 폼
   */
  @GetMapping(value = "/update_form")
  public String update_form(Model model, 
                            @RequestParam(name = "replyno", defaultValue = "0") int replyno) {
    ReplyVO replyVO = this.replyProc.read(replyno);
    model.addAttribute("replyVO", replyVO);
    return "reply/update";
  }

  /**
   * 댓글 수정 처리
   */
  @PostMapping(value = "/update")
  public String update(Model model, 
                       @ModelAttribute("replyVO") ReplyVO replyVO) {
    int cnt = this.replyProc.update(replyVO);

    if (cnt == 1) {
      model.addAttribute("msg", "댓글이 수정되었습니다.");
    } else {
      model.addAttribute("msg", "댓글 수정에 실패했습니다.");
    }

    model.addAttribute("url", "/reply/list_by_dogcontno?dogcontno=" + replyVO.getDogcontno());
    return "reply/msg";
  }

  /**
   * 댓글 삭제
   */
  @GetMapping(value = "/delete")
  public String delete(Model model, 
                       @RequestParam(name = "replyno", defaultValue = "0") int replyno,
                       @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno) {
    int cnt = this.replyProc.delete(replyno);
    this.replyProc.decreaseReplycnt(dogcontno);

    if (cnt == 1) {
      model.addAttribute("msg", "댓글이 삭제되었습니다.");
    } else {
      model.addAttribute("msg", "댓글 삭제에 실패했습니다.");
    }

    model.addAttribute("url", "/reply/list_by_dogcontno?dogcontno=" + dogcontno);
    return "reply/msg";
  }

  /**
   * 댓글 상세 보기
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
                     @RequestParam(name = "replyno", defaultValue = "0") int replyno) {
    ReplyVO replyVO = this.replyProc.read(replyno);
    model.addAttribute("replyVO", replyVO);
    return "reply/read";
  }
  
  /**
   * 댓글 추천 처리: 추천/추천 해제 (토글 방식)
   * 요청: POST /reply/good
   * 데이터: {"replyno": 10}
   */
  @PostMapping(value = "/good", produces = "application/json")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // {"replyno":10}
    
    JSONObject src = new JSONObject(json_src);
    int dogcontno = src.getInt("dogcontno");
    int replyno = src.getInt("replyno");
    System.out.println("-> replyno: " + replyno);

    JSONObject result = new JSONObject();

    if (this.usersProc.isUsers(session)) { // 로그인 확인
      int usersno = (int) session.getAttribute("usersno");

      // 이미 좋아요 했는지 확인
      HashMap<String, Object> map = new HashMap<>();
      map.put("replyno", replyno);
      map.put("usersno", usersno);

      int likeCnt = this.replyLikeProc.isLiked(map);

      if (likeCnt == 1) {
        // 추천 해제
        this.replyLikeProc.delete(map);
        System.out.println("-> 추천 해제: " + replyno + ", 사용자: " + usersno);
      } else {
        // 추천 추가
        ReplyLikeVO vo = new ReplyLikeVO();
        vo.setReplylikeno(this.replyLikeProc.getReplyLikeSeq());
        vo.setReplyno(replyno);
        vo.setUsersno(usersno);
        this.replyLikeProc.create(vo);
        System.out.println("-> 추천: " + replyno + ", 사용자: " + usersno);
      }

      // 추천 상태와 총 개수 다시 조회
      int isLiked = this.replyLikeProc.isLiked(map); // 다시 조회
      int totalLikeCount = this.replyLikeProc.countByReplyno(replyno);

      result.put("isUsers", 1);       // 로그인 사용자
      result.put("isLiked", isLiked); // 1=추천함, 0=추천 안 함
      result.put("likeCount", totalLikeCount);

    } else {
      result.put("isUsers", 0); // 로그인 안 된 사용자
    }

    System.out.println("-> result.toString(): " + result.toString());
    return result.toString();
  }

}
