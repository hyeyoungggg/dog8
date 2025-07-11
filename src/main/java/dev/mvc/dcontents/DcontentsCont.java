package dev.mvc.dcontents;

import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.contentsgood.ContentsgoodProcInter;
import dev.mvc.contentsgood.ContentsgoodVO;
import dev.mvc.dog.DogProcInter;
import dev.mvc.dog.DogVO;
import dev.mvc.dog.DogVOMenu;
import dev.mvc.dog_tool.Tool;
import dev.mvc.dog_tool.Upload;
import dev.mvc.reply.ReplyProcInter;
import dev.mvc.reply.ReplyVO;
import dev.mvc.replylike.ReplyLikeProcInter;
import dev.mvc.users.UsersProcInter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/dcontents")
@Controller
public class DcontentsCont {
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc") // @Service("dev.mvc.member.MemberProc")
  private UsersProcInter usersProc;

  @Autowired
  @Qualifier("dev.mvc.dog.DogProc") // @Component("dev.mvc.cate.CateProc")
  private DogProcInter dogProc;
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc") // @Component("dev.mvc.cate.CateProc")
  private ReplyProcInter replyProc;
  
  @Autowired
  @Qualifier("dev.mvc.replylike.ReplyLikeProc") // @Component("dev.mvc.cate.CateProc")
  private ReplyLikeProcInter replyLikeProc;

  @Autowired
  @Qualifier("dev.mvc.dcontents.DcontentsProc") // @Component("dev.mvc.contents.ContentsProc")
  private DcontentsProcInter dcontentsProc;
  
  @Autowired
  @Qualifier("dev.mvc.contentsgood.ContentsgoodProc") 
  private ContentsgoodProcInter contentsgoodProc;

  public DcontentsCont() {
    System.out.println("-> DcontentsCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   *
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
                                @RequestParam(name="url", defaultValue = "") String url
                                ) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, contents 테이블은 FK로 cateno를 사용함.
  // http://localhost:9091/dcontents/create X
  // http://localhost:9091/dcontents/create?cateno=1 // dogid 변수값을 보내는 목적
  // http://localhost:9091/dcontents/create?cateno=2
  // http://localhost:9091/dcontents/create?cateno=5
  @GetMapping(value = "/create")
  public String create(Model model, @ModelAttribute("dcontentsVO") DcontentsVO dcontentsVO,
      @RequestParam(name = "dogid", defaultValue = "0") int dogid) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    DogVO dogVO = this.dogProc.read(dogid); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("dogVO", dogVO);

    return "dcontents/create"; // /templates/dcontents/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/dcontents/create
   *
   * @return
   */
  @PostMapping(value = "/create")
  public String create_proc(HttpServletRequest request, HttpSession session, Model model,
      @ModelAttribute("dcontentsVO") DcontentsVO dcontentsVO, RedirectAttributes ra) {

    DogVO dogVO = dogProc.read(dcontentsVO.getDogid());
    
    if (usersProc.isAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Dcontents.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = dcontentsVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1)) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          dcontentsVO.setFile1(file1); // 순수 원본 파일명
          dcontentsVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          dcontentsVO.setThumb1(thumb1); // 원본이미지 축소판
          dcontentsVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", Tool.UPLOAD_FILE_CHECK_FAIL); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용
          return "redirect:/contents/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int usersno = (int) session.getAttribute("usersno"); // usersno FK
      dcontentsVO.setUsersno(usersno);
      int cnt = this.dcontentsProc.create(dcontentsVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> dogcontno: " + contentsVO.getContentsno());
      // mav.addObject("dogcontno", contentsVO.getContentsno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "contents/msg";

        // type 3 권장
        // return "redirect:/contents/list_all"; // /templates/contents/list_all.html

        // System.out.println("-> contentsVO.getCateno(): " + contentsVO.getCateno());
        // ra.addFlashAttribute("cateno", contentsVO.getCateno()); // controller ->
        // controller: X
        
        this.dcontentsProc.update_cnt_up(dcontentsVO.getDogid());
        this.dcontentsProc.update_cnt_breed_up(dogVO.getBreed());

        ra.addAttribute("dogid", dcontentsVO.getDogid()); // controller -> controller: O
        return "redirect:/dcontents/list_by_dogid";
//        return "redirect:/dcontents/list_all"; // templates/contents/list_all.html

        // return "redirect:/contents/list_by_cateno?cateno=" + contentsVO.getCateno();
        // // /templates/contents/list_by_cateno.html
      } else {
        ra.addFlashAttribute("code", Tool.CREATE_FAIL); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/dcontents/msg"); // msg.html, redirect parameter 적용
        return "redirect:/dcontents/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/users/login_cookie_need?url=/dcontents/create>dogid=" + dcontentsVO.getDogid(); // /member/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/dcontents/list_all
   *
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    if (this.usersProc.isAdmin(session)) { // 관리자만 조회 가능
      ArrayList<DcontentsVO> list = this.dcontentsProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (DcontentsVO contentsVO : list) {
//        String title = contentsVO.getTitle();
//        String content = contentsVO.getContent();
//
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content);
//
//        contentsVO.setTitle(title);
//        contentsVO.setContent(content);
//
//      }

      model.addAttribute("list", list);
      return "dcontents/list_all";

    } else {
      return "redirect:/users/login_cookie_need";

    }

  }

//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9091/dcontents/list_by_cateno?cateno=5
//   * http://localhost:9091/dcontents/list_by_cateno?cateno=6
//   * @return
//   */
//  @GetMapping(value="/list_by_dogid")
//  public String list_by_dogid(HttpSession session, Model model,
//                                     @RequestParam(name="dogid", defaultValue = "") int dogid
//                                     ) {
//    ArrayList<DogVOMenu> menu = this.dogProc.menu();
//    model.addAttribute("menu", menu);
//
//     DogVO dogVO = this.dogProc.read(dogid);
//     model.addAttribute("dogVO", dogVO);
//
//    ArrayList<DcontentsVO> list = this.dcontentsProc.list_by_dogid(dogid);
//    model.addAttribute("list", list);
//
//    // System.out.println("-> size: " + list.size());
//
//    return "dcontents/list_by_dogid"; // templet/contents/list_by_cateno.html
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9091/contents/list_by_cateno?cateno=5
//   * http://localhost:9091/contents/list_by_cateno?cateno=6
//   * @return
//   */
//  @GetMapping(value="/list_by_cateno")
//  public String list_by_cateno_search(HttpSession session, Model model,
//                                                    int cateno, @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<dogVOMenu> menu = this.DogProc.menu();
//    model.addAttribute("menu", menu);
//
//     DogVO dogVO = this.DogProc.read(cateno);
//     model.addAttribute("dogVO", dogVO);
//
//     word = Tool.checkNull(word).trim();
//
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("cateno", cateno);
//     map.put("word", word);
//
//    ArrayList<DcontentsVO> list = this.contentsProc.list_by_cateno_search(map);
//    model.addAttribute("list", list);
//
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//
//    int search_count = this.contentsProc.list_by_cateno_search_count(map);
//    model.addAttribute("search_count", search_count);
//
//    return "contents/list_by_cateno_search"; // /templates/contents/list_by_cateno_search.html
//  }

  /**
   * 유형 3 카테고리별 목록 + 검색 + 페이징
   * http://localhost:9091/contents/list_by_cateno?cateno=5
   * http://localhost:9091/contents/list_by_cateno?cateno=6
   *
   * @return
   */
  @GetMapping(value = "/list_by_dogid")
  public String list_by_dogid_search_paging(HttpSession session, Model model,
      @RequestParam(name = "dogid", defaultValue = "0") int dogid,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> cateno: " + cateno);

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    DogVO dogVO = this.dogProc.read(dogid);
    model.addAttribute("dogVO", dogVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("dogid", dogid);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<DcontentsVO> list = this.dcontentsProc.list_by_dogid_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.dcontentsProc.list_by_dogid_search_count(map);
    String paging = this.dcontentsProc.pagingBox(dogid, now_page, word, "/dcontents/list_by_dogid", search_count,
        Dcontents.RECORD_PER_PAGE, Dcontents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Dcontents.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "dcontents/list_by_dogid_search_paging"; // /templates/contents/list_by_cateno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/dcontents/list_by_cateno?cateno=5
   * http://localhost:9091/dcontents/list_by_cateno?cateno=6
   *
   * @return
   */
  @GetMapping(value = "/list_by_dogid_grid")
  public String list_by_dogid_search_paging_grid(HttpSession session, Model model,
      @RequestParam(name = "dogid", defaultValue = "0") int dogid,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> dogid: " + dogid);

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    DogVO dogVO = this.dogProc.read(dogid);
    model.addAttribute("dogVO", dogVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("dogid", dogid);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<DcontentsVO> list = this.dcontentsProc.list_by_dogid_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.dcontentsProc.list_by_dogid_search_count(map);
    String paging = this.dcontentsProc.pagingBox(dogid, now_page, word, "/dcontents/list_by_dogid_grid", search_count,
        Dcontents.RECORD_PER_PAGE, Dcontents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Dcontents.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/contents/list_by_cateno_search_paging_grid.html
    return "dcontents/list_by_dogid_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/contents/read?dogcontno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model, 
      @RequestParam(name="dogcontno", defaultValue = "0") int dogcontno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);
    
    // 댓글 목록 조회
    List<ReplyVO> replyList = this.replyProc.list_by_dogcontno(dogcontno);
    model.addAttribute("replyList", replyList);

    DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno);
    model.addAttribute("dcontentsVO", dcontentsVO);

//    String title = contentsVO.getTitle();
//    String content = contentsVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    contentsVO.setTitle(title);
//    contentsVO.setContent(content);  

    long size1 = dcontentsVO.getSize1();
    String size1_label = Tool.unit(size1);
    dcontentsVO.setSize1_label(size1_label);

    DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid());
    model.addAttribute("dogVO", dogVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(dogcontno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("dogcontno", dogcontno);
    
    int hartCnt = 0;
    if (session.getAttribute("usersno") != null ) { // 회원인 경우만 카운트 처리
      int usersno = (int)session.getAttribute("usersno");
      map.put("usersno", usersno);
      
      hartCnt = this.contentsgoodProc.hartCnt(map);
    } 
    
    model.addAttribute("hartCnt", hartCnt);
   
    Integer usersno = (Integer) session.getAttribute("usersno");
    if (usersno == null) usersno = 0;

    for (ReplyVO reply : replyList) {
      int likeCount = replyLikeProc.countByReplyno(reply.getReplyno());
      boolean isLiked = replyLikeProc.isLiked(Map.of("replyno", reply.getReplyno(), "usersno", usersno)) == 1;
      reply.setLikeCount(likeCount);
      reply.setLiked(isLiked);
    }

    model.addAttribute("replyList", replyList);
    // -------------------------------------------------------------------------------------------
   
    return "dcontents/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/dcontents/map?dogcontno=1
   *
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno); // map 정보 읽어 오기
    model.addAttribute("dcontentsVO", dcontentsVO); // request.setAttribute("contentsVO", contentsVO);

    DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid()); // 그룹 정보 읽기
    model.addAttribute("dogVO", dogVO);

    return "dcontents/map";
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/dcontents/map
   *
   * @param contentsVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno,
      @RequestParam("map") String map) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("dogcontno", dogcontno);
    hashMap.put("map", map);

    this.dcontentsProc.map(hashMap);

    return "redirect:/dcontents/read?dogcontno=" + dogcontno;
  }

  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9091/dcontents/youtube?dogcontno=1
   *
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno); // map 정보 읽어 오기
    model.addAttribute("dcontentsVO", dcontentsVO); // request.setAttribute("contentsVO", contentsVO);

    DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid()); // 그룹 정보 읽기
    model.addAttribute("dogVO", dogVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "dcontents/youtube"; // forward
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9091/dcontents/youtube
   *
   * @param contentsVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, RedirectAttributes ra,
      @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno,
      @RequestParam(name = "youtube", defaultValue = " ") String youtube,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("dogcontno", dogcontno);
    hashMap.put("youtube", youtube);

    this.dcontentsProc.youtube(hashMap);

    ra.addAttribute("dogcontno", dogcontno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/contents/read?dogcontno=" + dogcontno;
    return "redirect:/dcontents/read";
  }

  /**
   * 수정 폼 http:// localhost:9091/contents/update_text?dogcontno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model,
      @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno, RedirectAttributes ra,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.usersProc.isAdmin(session)) { // 관리자로 로그인한경우
      DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno);
      model.addAttribute("dcontentsVO", dcontentsVO);

      DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid());
      model.addAttribute("dogVO", dogVO);

      return "dcontents/update_text"; // /templates/contents/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
//     // 로그인 후 텍스트 수정폼이 자동으로 열린다
      return "redirect:/users/login_cookie_need?url=/dcontents/update_text?dogcontno=" + dogcontno; // /templates/users/login_cookie_need.html
    }

  }

  /**
   * 수정 처리 http://localhost:9091/dcontents/update_text?dogcontno=1
   *
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, 
                                  Model model, 
                                  DcontentsVO dcontentsVO, 
                                  RedirectAttributes ra,
                                  @RequestParam(name = "search_word", defaultValue = "") String search_word, // contentsVO.word와 구분 필요
                                  @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
                                  ra.addAttribute("word", search_word);
                                  ra.addAttribute("now_page", now_page);

    if (this.usersProc.isAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<>();
      map.put("dogcontno", dcontentsVO.getDogcontno());
      map.put("passwd", dcontentsVO.getPasswd());

      if (this.dcontentsProc.password_check(map) == 1) { // 패스워드 일치
        this.dcontentsProc.update_text(dcontentsVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("dogcontno", dcontentsVO.getDogcontno());
        ra.addAttribute("dogid", dcontentsVO.getDogid());
        return "redirect:/dcontents/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "password_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "dcontents/msg"); // msg.html, redirect parameter 적용

        return "redirect:/dcontents/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      return "redirect:/users/login_cookie_need?url=/dcontents/update_text?dogcontno=" + dcontentsVO.getDogcontno(); // @GetMapping(value
                                                                                                                     // =
                                                                                                                     // "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/contents/update_file?dogcontno=1
   *
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model,
      @RequestParam(name = "dogcontno", defaultValue = "0") int dogcontno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
    if (this.usersProc.isAdmin(session)) {
      ArrayList<DogVOMenu> menu = this.dogProc.menu();
      model.addAttribute("menu", menu);

      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno);
      model.addAttribute("dcontentsVO", dcontentsVO);

      DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid());
      model.addAttribute("dogVO", dogVO);

      return "dcontents/update_file";

    } else {
      // 로그인후 파일 수정폼이 자동으로 열림.
      return "redirect:/users/login_cookie_need?url=/dcontents/update_file?dogcontno=" + dogcontno;

    }

  }

  /**
   * 파일 수정 처리 http://localhost:9091/dcontents/update_file
   *
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file_proc(HttpSession session, Model model, RedirectAttributes ra,  
                                  DcontentsVO dcontentsVO,
                                  @RequestParam(name="word", defaultValue = "") String word,
                                  @RequestParam(name="now_page", defaultValue = "0") int now_page
                                  ) {

    if (this.usersProc.isAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      DcontentsVO dcontentsVO_old = dcontentsProc.read(dcontentsVO.getDogcontno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = dcontentsVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = dcontentsVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Dcontents.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = dcontentsVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      dcontentsVO.setFile1(file1);
      dcontentsVO.setFile1saved(file1saved);
      dcontentsVO.setThumb1(thumb1);
      dcontentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.dcontentsProc.update_file(dcontentsVO); // Oracle 처리
      ra.addAttribute("dogcontno", dcontentsVO.getDogcontno());
      ra.addAttribute("dogid", dcontentsVO.getDogid());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);

      return "redirect:/dcontents/read";
    } else {
      ra.addAttribute("url", "users/login_cookie_need");
      return "redirect:/dcontents/post2get"; // GET
    }
  }

  /**
   * 파일 삭제 폼 http://localhost:9091/dcontents/delete?dogcontno=1
   *
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                            @RequestParam(name="dogid", defaultValue = "0") int dogid,
                            @RequestParam(name="dogcontno", defaultValue = "0") int dogcontno,
                            @RequestParam(name="word", defaultValue = "") String word, 
                            @RequestParam(name="now_page", defaultValue = "1") int now_page
                            ) {
    if (this.usersProc.isAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("dogid", dogid);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      ArrayList<DogVOMenu> menu = this.dogProc.menu();
      model.addAttribute("menu", menu);

      DcontentsVO dcontentsVO = this.dcontentsProc.read(dogcontno);
      model.addAttribute("dcontentsVO", dcontentsVO);

      DogVO dogVO = this.dogProc.read(dcontentsVO.getDogid());
      model.addAttribute("dogVO", dogVO);

      return "dcontents/delete"; // forward

    } else {

      return "redirect:/users/login_cookie_need?url=/dcontents/delete?dogcontno=" + dogcontno;
    }

  }

  /**
   * 삭제 처리 http://localhost:9091/dcontents/delete
   *
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(RedirectAttributes ra, 
                            DcontentsVO dcontentsVO,
                            @RequestParam(name="dogid", defaultValue = "0") int dogid,
                            @RequestParam(name="dogcontno", defaultValue = "0") int dogcontno,
                            @RequestParam(name="word", defaultValue = "") String word, 
                            @RequestParam(name="now_page", defaultValue = "1") int now_page
                            ) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    DcontentsVO dcontentsVO_read = dcontentsProc.read(dogcontno);
    DogVO dogVO = dogProc.read(dcontentsVO.getDogid());

    String file1saved = dcontentsVO_read.getFile1saved();
    String thumb1 = dcontentsVO_read.getThumb1();

    String uploadDir = Dcontents.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved); // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1); // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------

    this.dcontentsProc.delete(dogcontno); // DBMS 삭제
    
    this.dcontentsProc.update_cnt_down(dcontentsVO.getDogid());
    this.dcontentsProc.update_cnt_breed_down(dogVO.getBreed());

    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생

    HashMap<String, Object> map = new HashMap<>();
    map.put("dogid", dogid);
    map.put("word", word);

    if (this.dcontentsProc.list_by_dogid_search_count(map) % Dcontents.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("dogid", dogid);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    return "redirect:/dcontents/list_by_dogid";

  }
  
  /**
   * 추천 처리 http://localhost:9091/contents/good
   * 
   * @return
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src){ 
    System.out.println("-> json_src: " + json_src); // json_src: {"dogcontno":"5"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int dogcontno = (int)src.get("dogcontno"); // 값 가져오기
    System.out.println("-> dogcontno: " + dogcontno);
        
    if (this.usersProc.isUsers(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int usersno = (int)session.getAttribute("usersno");
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("dogcontno", dogcontno);
      map.put("usersno", usersno);
      
      int good_cnt = this.contentsgoodProc.hartCnt(map);
      System.out.println("-> good_cnt: " + good_cnt);
      
      if (good_cnt == 1) { // 이미 추천을한 회원인지 검사
        System.out.println("-> 추천 해제: " + dogcontno + ' ' + usersno);
        
        ContentsgoodVO contentsgoodVO = this.contentsgoodProc.readByContentsnoUsersno(map);
        
        this.contentsgoodProc.delete(contentsgoodVO.getContentsgoodno()); // 추천 삭제
        this.dcontentsProc.decreaseRecom(dogcontno); // 카운트 감소
      } else {
        System.out.println("-> 추천: " + dogcontno + ' ' + usersno);
        
        ContentsgoodVO contentsgoodVO_new = new ContentsgoodVO();
        contentsgoodVO_new.setDogcontno(dogcontno);
        contentsgoodVO_new.setUsersno(usersno);
        
        this.contentsgoodProc.create(contentsgoodVO_new);
        this.dcontentsProc.increaseRecom(dogcontno); // 카운트 증가
      }
      
      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
      int hartCnt = this.contentsgoodProc.hartCnt(map);
      int recom = this.dcontentsProc.read(dogcontno).getRecom();
            
      JSONObject result = new JSONObject();
      result.put("isUsers", 1); // 로그인: 1, 비회원: 0
      result.put("hartCnt", hartCnt); // 추천 여부, 추천:1, 비추천: 0
      result.put("recom", recom);   // 추천인수
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
      
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isUsers", 0); // 로그인: 1, 비회원: 0
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }

  }

}