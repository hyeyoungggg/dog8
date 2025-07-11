package dev.mvc.replylike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/replylike")
public class ReplyLikeCont {

  @Autowired
  @Qualifier("dev.mvc.replylike.ReplyLikeProc")
  private ReplyLikeProcInter replyLikeProc;

  /** 댓글 좋아요 누르기 */
  @PostMapping("/like")
  @ResponseBody
  public Map<String, Object> like(@RequestParam("replyno") int replyno, 
                                           @RequestParam("dogcontno") int dogcontno, 
                                           HttpSession session) {
    
    System.out.println("-> replyno: " + replyno);
    System.out.println("-> dogcontno: " + dogcontno);
    System.out.println("-> usersno: " + session.getAttribute("usersno"));

    Map<String, Object> response = new HashMap<>();

    // 로그인한 사용자 번호 가져오기
    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) {
      response.put("success", false);
      response.put("message", "로그인이 필요합니다.");
      return response;
    }

    // 중복 방지 확인
    Map<String, Object> checkMap = new HashMap<>();
    checkMap.put("replyno", replyno);
    checkMap.put("usersno", usersno);

    if (replyLikeProc.isLiked(checkMap) == 0) {
      ReplyLikeVO vo = new ReplyLikeVO();
      vo.setReplyno(replyno);
      vo.setUsersno(usersno);
      vo.setDogcontno(dogcontno);
      replyLikeProc.create(vo);
    }

    int likeCount = replyLikeProc.countByReplyno(replyno);
    response.put("success", true);
    response.put("likeCount", likeCount);
    return response;
  }

  /** 댓글 좋아요 취소 */
  @PostMapping("/unlike")
  public Map<String, Object> unlike(@RequestParam("replyno") int replyno, HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) {
      response.put("success", false);
      response.put("message", "로그인이 필요합니다.");
      return response;
    }

    Map<String, Object> map = new HashMap<>();
    map.put("replyno", replyno);
    map.put("usersno", usersno);

    replyLikeProc.delete(map);

    int likeCount = replyLikeProc.countByReplyno(replyno);
    response.put("success", true);
    response.put("likeCount", likeCount);
    return response;
  }

  /** 현재 사용자가 해당 댓글에 좋아요 눌렀는지 여부 (프론트에서 하트 아이콘 색상 결정용) */
  @GetMapping("/isLiked")
  @ResponseBody
  public Map<String, Object> isLiked(@RequestParam("replyno") int replyno, HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) {
      response.put("liked", false);
      return response;
    }

    Map<String, Object> map = new HashMap<>();
    map.put("replyno", replyno);
    map.put("usersno", usersno);

    boolean liked = replyLikeProc.isLiked(map) > 0;
    response.put("liked", liked);
    return response;
  }
  
  @GetMapping("/list_all")
  public List<ReplyLikeVO> list_all() {
    return replyLikeProc.list_all(); // DAO에 list_all 메서드 필요
  }
  
  @PostMapping("/delete")
  public String delete(@RequestParam("replylikeno") int replylikeno) {
    replyLikeProc.deleteByPk(replylikeno);
    return "redirect:/replylike/list_all_com"; // 또는 다른 목록 페이지
  }
  
  @GetMapping("/list_all_join")
  public ModelAndView list_all_join() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/replylike/list_all_com_join"); // ← 출력할 HTML 경로
    mav.addObject("list", replyLikeProc.list_all_join()); // JOIN 결과 리스트
    return mav;
  }


}
