package dev.mvc.dog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.dog_tool.Tool;
import dev.mvc.users.UsersProc;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/dog")
public class DogCont {

  @Autowired
  @Qualifier("dev.mvc.dog.DogProc")
  private DogProcInter dogProc;

  @Autowired // Spring이 CateProcInter를 구현한 CateProc 클래스의 객체를 생성하여 할당
  @Qualifier("dev.mvc.users.UsersProc")
  private UsersProc usersProc;

  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 , @GetMapping(value="/list_search") */
  private String list_url = "/dog/list_search";

  public DogCont() {
    System.out.println("-> DogCont created.");
  }

  /**
   * 강아지 등록 폼
   * URL 예: http://localhost:9091/dog/create
   * @return 강아지 등록 폼 템플릿
   */
  @GetMapping(value="/create")
  public String createForm(@ModelAttribute("dogVO") DogVO dogVO) {

    return "dog/create";  // /templates/dog/dog.html
  }

  /**
   * 등록 처리
   * Model model: controller -> html로 데이터 전송 제공
   * @Valid: @NotEmpty, @Size, @NotNull, @Min, @Max, @Pattern... 규칙 위반 검사 지원
   * CateVO cateVO: FORM 태그의 값 자동 저장, Integer.parseInt(request.getParameter("seqno")) 자동 실행
   * BindingResult bindingResult: @Valid의 결과 저장
   * @param model
   * @return
   */
  @PostMapping(value="/create")
  public String create(Model model,
                           @Valid DogVO dogVO, BindingResult bindingResult,
                           @RequestParam(name = "word", defaultValue = "") String word,
                           RedirectAttributes ra
                           ) {

      ArrayList<DogVO> list = this.dogProc.list_search(word);
      model.addAttribute("list", list);

      // 유효성 검사
      if (bindingResult.hasErrors()) {
          return "dog/create";  // /templates/dog/dog.html
      }

      // 강아지 등록 처리
      int cnt = this.dogProc.create(dogVO);

      if (cnt == 1) {

//          return "redirect:/dog/list_all"; // @GetMapping(value="/dog_list") 호출
        ra.addAttribute("word", word);
        return "redirect:/dog/list_search";

      } else {

          model.addAttribute("code", Tool.CREATE_FAIL);

      }

      model.addAttribute("cnt", cnt);
      return "dog/msg";  // /templates/dog/dog_msg.html
  }

  /**
   * 전체 목록
   * http://localhost:9091/cate/list_all
   * @param model
   * @return
   */
  @GetMapping(value="/list_all")
  public String list_all(Model model,
                          @ModelAttribute("dogVO") DogVO dogVO,
                          @RequestParam(name = "word", defaultValue = "") String word
                          ) {

    dogVO.setBreed("");
    dogVO.setName("");

    ArrayList<DogVO> list = this.dogProc.list_search(word);
    model.addAttribute("list", list);

 // 2단 메뉴
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

 // 카테고리 그룹 목록
    ArrayList<String> grpset = this.dogProc.grpset();
    dogVO.setBreed(String.join("/", grpset));
    System.out.println("-> dogVO.getGrp():" + dogVO.getBreed());

    model.addAttribute("word", word);

    int list_search_count = this.dogProc.list_search_count(word); // 검색된 레코드 갯수
    model.addAttribute("list_search_count", list_search_count);

    return "dog/list_all"; // /templates/dog/dog_list.html

  }

//  /**
//   * 강아지 검색 목록 조회
//   * http://localhost:9091/dog/dog_list
//   * @param model
//   * @return
//   */
//  @GetMapping(value="/list_search")
//  public String list_search(Model model,
//                                @ModelAttribute("dogVO") DogVO dogVO,
//                                @RequestParam(name = "word", defaultValue = "") String word
//                                ) {
//
//    dogVO.setBreed("");
//    dogVO.setName("");
//
//    ArrayList<DogVO> list = this.dogProc.list_search(word);
//    model.addAttribute("list", list);
//
//    ArrayList<DogVOMenu> menu = this.dogProc.menu();
//    model.addAttribute("menu", menu);
//
//    ArrayList<String> grpset = this.dogProc.grpset();
//    dogVO.setBreed(String.join("/", grpset));
//    System.out.println("-> dogVO.getBreed():" + dogVO.getBreed());
//
//    model.addAttribute("word", word);
//
//    int list_search_count = this.dogProc.list_search_count(word); // 검색된 레코드 갯수
//    model.addAttribute("list_search_count", list_search_count);
//
//    return "dog/list_search"; // /templates/dog/dog_list.html
//  }

//  /**
//   * 등록 폼 및 검색 목록 + 페이징
//   * http://localhost:9091/cate/list_search
//   * http://localhost:9091/cate/list_search?word=&now_page=
//   * http://localhost:9091/cate/list_search?word=까페&now_page=1
//   * @param model
//   * @return
//   */
//
//  @GetMapping(value="/list_search")
//  public String list_search_paging(Model model,
//                                   @RequestParam(name="word", defaultValue = "") String word,
//                                   @RequestParam(name="now_page", defaultValue="1") int now_page) {
//    DogVO dogVO = new DogVO();
//    // cateVO.setGenre("분류");
//    // cateVO.setName("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달
//
//    // 카테고리 그룹 목록
//    ArrayList<String> list_grp = this.dogProc.grpset();
//    dogVO.setBreed(String.join("/", list_grp));
//
//
//    model.addAttribute("dogVO", dogVO); // 등록폼 카테고리 그룹 초기값
//
//    word = Tool.checkNull(word);            // null -> ""
//
//    ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
//    model.addAttribute("list", list);
//    System.out.println("->list.size()" + list.size());
//
////    ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
////    model.addAttribute("menu", menu);
//
//    ArrayList<DogVOMenu> menu = this.dogProc.menu();
//    model.addAttribute("menu", menu);
//
//    int search_cnt = list.size();
//    model.addAttribute("search_cnt", search_cnt);
//
//    model.addAttribute("word", word); // 검색어
//    System.out.println("->word" + word);
//
//    // --------------------------------------------------------------------------------------
//    // 페이지 번호 목록 생성
//    // --------------------------------------------------------------------------------------
//    int search_count = this.dogProc.list_search_count(word);
//    String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
//    model.addAttribute("paging", paging);
//    System.out.println("->paging: " + paging);
//    model.addAttribute("now_page", now_page);
//    System.out.println("-> now_page: " + now_page);
//
//    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//    int no = search_count - ((now_page - 1) * this.record_per_page);
//    model.addAttribute("no", no);
//    // --------------------------------------------------------------------------------------
//
//    return "dog/list_search";  // /templates/cate/list_search.html
//  }

  /**
   * 등록 폼 및 검색 목록 + 페이징
   * http://localhost:9091/cate/list_search
   * http://localhost:9091/cate/list_search?word=&now_page=
   * http://localhost:9091/cate/list_search?word=까페&now_page=1
   * @param model
   * @return
   */
  @GetMapping(value="/list_search")
  public String list_search_paging(HttpSession session,
                                              Model model,
                                              @RequestParam(name="word", defaultValue = "") String word,
                                              @RequestParam(name="now_page", defaultValue="1") int now_page) {
    if (this.usersProc.isAdmin(session)) {
      DogVO dogVO = new DogVO();
      // dogVO.setUseregenre("분류");
      // dogVO.setUserename("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달

      // 카테고리 그룹 목록
      ArrayList<String> list_grp = this.dogProc.grpset();
    dogVO.setBreed(String.join("/", list_grp));


    model.addAttribute("dogVO", dogVO); // 등록폼 카테고리 그룹 초기값

    word = Tool.checkNull(word);

      ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

//      ArrayList<CateVO> menu = this.cateProc.list_all_categrp_y();
//      model.addAttribute("menu", menu);

      ArrayList<DogVOMenu> menu = this.dogProc.menu();
      model.addAttribute("menu", menu);

      int search_cnt = this.dogProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);

      model.addAttribute("word", word); // 검색어

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.dogProc.list_search_count(word);
      String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "dog/list_search";  // /templates/cate/list_search.html
    } else {
      return "redirect:/users/login_cookie_need?url=/dog/list_search"; // redirect
    }

  }

  /**
   * 단일 강아지 상세 조회
   * http://localhost:9091/dog/read?dogid=1
   * @param model
   * @param dogid
   * @return
   */
  @GetMapping(value="/read/{dogid}")
  public String read(Model model, @PathVariable("dogid") Integer dogid,
                        @RequestParam(name = "word", defaultValue = "") String word,
                        @RequestParam(name="now_page", defaultValue="1") int now_page
                        ) {

    System.out.println("-> read cateno: " + dogid);

    DogVO dogVO = this.dogProc.read(dogid);
    model.addAttribute("dogVO", dogVO);

//    ArrayList<DogVO> list = this.dogProc.list_all();
//    // 모델에 강아지 목록 추가
//    model.addAttribute("list", list);

// 2단 목록
    ArrayList<DogVOMenu> menu = this.dogProc.menu(); // return -> ArrayList<CateVOMenu>
    model.addAttribute("menu", menu);

// // 카테고리 그룹 목록
//    ArrayList<String> grpset = this.dogProc.grpset();
//    dogVO.setBreed(String.join("/", grpset));
//    System.out.println("-> cateVO.getBreed(): " + dogVO.getBreed());

    model.addAttribute("word", word);

    ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);

    model.addAttribute("word", word);
 // System.out.println("-> word null 체크: " + word);

    int list_search_count = this.dogProc.list_search_count(word); // 검색된 레코드 갯수
    model.addAttribute("list_search_count", list_search_count);

 // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.dogProc.list_search_count(word);
    String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    // --------------------------------------------------------------------------------------

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "dog/read"; // /templates/dog/dog_read.html
  }

  /**
   * 수정폼
   * http://localhost:9091/cate/update/1
   * @param model
   * @return
   */
  @GetMapping(value = "/update/{dogid}")
  public String update(Model model,
                            @PathVariable("dogid") Integer dogid,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue="1") int now_page
                            ) {

    System.out.println("검색어: " + word);

    DogVO dogVO = this.dogProc.read(dogid);  // dogId 수정
    model.addAttribute("dogVO", dogVO);  // dogVO로 수정

    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

//    ArrayList<String> grpset = this.dogProc.grpset();
//    dogVO.setBreed(String.join("/", grpset));
//    System.out.println("-> cateVO.getGrp(): " + dogVO.getBreed());

    ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
    // 모델에 강아지 목록 추가
    model.addAttribute("list", list);

    model.addAttribute("word", word);

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.dogProc.list_search_count(word);
    String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    // --------------------------------------------------------------------------------------

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "dog/update";  // 여기서 update.html로 이동
  }

  /**
   * 수정 처리
   * Model model: controller -> html로 데이터 전송 제공
   * @Valid: @NotEmpty, @Size, @NotNull, @Min, @Max, @Pattern... 규칙 위반 검사 지원
   * CateVO cateVO: FORM 태그의 값 자동 저장, Integer.parseInt(request.getParameter("seqno")) 자동 실행
   * BindingResult bindingResult: @Valid의 결과 저장
   * @param model
   * @return
   */
  @PostMapping(value = "/update")
  public String update(Model model,
                            @Valid DogVO dogVO, BindingResult bindingResult,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name = "now_page", defaultValue = "") int now_page,
                            RedirectAttributes ra
                            ) {

      ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      if (bindingResult.hasErrors()) {
          return "dog/update";  // 잘못된 데이터가 입력되었을 경우 다시 수정 폼으로
      }

      int cnt = this.dogProc.update(dogVO);  // dogProc.update로 수정
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {
        //   model.addAttribute("code", Tool.UPDATE_SUCCESS);
        //   model.addAttribute("name", dogVO.getName());  // dogVO로 수정
//        return "redirect:/dog/update/" + dogVO.getDogid(); // @GetMapping(value="/update")
        ra.addAttribute("now_page", now_page);
        ra.addAttribute("word", word);
        return "redirect:/dog/update/" + dogVO.getDogid();
      } else {
          model.addAttribute("code", Tool.UPDATE_FAIL);
      }

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.dogProc.list_search_count(word);
      String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
   // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      model.addAttribute("cnt", cnt);
      return "dog/msg";  // dog_msg.html로 이동
  }

  /**
   * 삭제폼
   * http://localhost:9091/dog/dog_delete?dogid=1
   * @param model
   * @param dogid
   * @return
   */
  @GetMapping(value = "/delete/{dogid}")
  public String delete(Model model,
                            @PathVariable("dogid") Integer dogid,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue="1") int now_page
                            ) {
    System.out.println("-> dogid: " + dogid);

    DogVO dogVO = this.dogProc.read(dogid);
    System.out.println("-> dogVO.dogid" + dogVO.getDogid());
    model.addAttribute("dogVO", dogVO);
    
    int count_by = this.dogProc.count_by_dogid(dogid);
    model.addAttribute("count_by", count_by);
    

    ArrayList<DogVO> list = this.dogProc.list_search_paging(word, now_page, this.record_per_page);
    System.out.println("-> delete form list.size():" + list.size());
    model.addAttribute("list", list);

 // 2단 메뉴
    ArrayList<DogVOMenu> menu = this.dogProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word); // redirect 아니면 model에 저장

    // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.dogProc.list_search_count(word);
    String paging = this.dogProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
 // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // --------------------------------------------------------------------------------------

    return "dog/delete";  // 강아지 삭제 페이지
  }

  /**
   * 삭제 처리
   * @param model
   * @return
   */
  @PostMapping(value = "/delete/{dogid}")
  public String delete_process(Model model,
                                      @PathVariable("dogid") Integer dogid,
                                      @RequestParam(name = "word", defaultValue = "") String word,
                                      @RequestParam(name="now_page", defaultValue="1") int now_page,
                                      RedirectAttributes ra
                                      ) {
      DogVO dogVO = this.dogProc.read(dogid); // 삭제 정보 출력용으로 사전에 읽음
      model.addAttribute("dogVO", dogVO);
      
      int count_by = this.dogProc.count_by_dogid(dogid);
      model.addAttribute("count_by", count_by);
      
     
        int deletecnt = this.dogProc.delete_by_dogid(dogid);
        
        Map<String, Object> min = new HashMap<>();
        min.put("deletecnt", deletecnt);
        min.put("breed", dogVO.getBreed());
        
       this.dogProc.update_cnt_breed_min(min);

      int cnt = this.dogProc.delete(dogid); // 삭제 처리

      // word값
      ArrayList<DogVO> list = this.dogProc.list_search(word);
      model.addAttribute("list", list);

      if (cnt == 1) {
        //   model.addAttribute("code", Tool.DELETE_SUCCESS);

//          model.addAttribute("name", dogVO.getName());  // 강아지 이름 추가

        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.dogProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        // ----------------------------------------------------------------------------------------------------------

        // model.addAttribute("code", Tool.DELETE_SUCCESS);
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page);

        return "redirect:/dog/list_search"; // @GetMapping(value="/list_search")
      } else {
          model.addAttribute("code", Tool.DELETE_FAIL);
      }

      model.addAttribute("grp", dogVO.getBreed());
      model.addAttribute("name", dogVO.getName());
      model.addAttribute("cnt", cnt);

      return "dog/msg";  // 강아지 삭제 메시지 페이지
  }

  /**
   * 우선 순위 높임, 10 등 -> 1 등
   * http://localhost:9092/dog/update_seqno_forward/1
   */
  @GetMapping(value="/update_seqno_forward/{dogid}")
    public String update_seqno_forward(Model model,
                                                  @PathVariable("dogid") Integer dogid,
                                                  @RequestParam(name = "word", defaultValue = "") String word,
                                                  @RequestParam(name="now_page", defaultValue="1") int now_page,
                                                  RedirectAttributes ra
                                                  ) {
      System.out.println(" -> +++");

      this.dogProc.update_seqno_forward(dogid);

      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      return "redirect:/dog/list_search";  // @GetMapping(value="/list_all")
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등
   * http://localhost:9092/dog/update_seqno_forward/1
   */
  @GetMapping(value="/update_seqno_backward/{dogid}")
    public String update_seqno_backward(Model model,
                                                    @PathVariable("dogid") Integer dogid,
                                                    @RequestParam(name = "word", defaultValue = "") String word,
                                                    @RequestParam(name="now_page", defaultValue="1") int now_page,
                                                    RedirectAttributes ra
                                                    ) {

      this.dogProc.update_seqno_backward(dogid);

      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      return "redirect:/dog/list_search";  // @GetMapping(value="/list_all")
  }

  /**
   * <!-- 카테고리 공개 설정 -->
   * http://localhost:9091/dog/update_visible_y/1
   */
  @GetMapping(value="/update_visible_y/{dogid}")
  public String update_visible_y(Model model,
                                        @PathVariable("dogid")Integer dogid,
                                        @RequestParam(name = "word", defaultValue = "") String word,
                                        @RequestParam(name="now_page", defaultValue="1") int now_page,
                                        RedirectAttributes ra
                                        ) {
    System.out.println("->update_visible_y()");

    this.dogProc.update_visible_y(dogid);

    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    return "redirect:/dog/list_search";  // /templates/cate/msg.html
  }

  /**
   * <!-- 카테고리 비공개 설정 -->
   * http://localhost:9091/dog/update_visible_n/1
   */
  @GetMapping(value="/update_visible_n/{dogid}")
  public String update_visible_n(Model model,
                                        @PathVariable("dogid")Integer dogid,
                                        @RequestParam(name = "word", defaultValue = "") String word,
                                        @RequestParam(name="now_page", defaultValue="1") int now_page,
                                        RedirectAttributes ra
                                        ) {
    System.out.println("->update_visible_n()");

    this.dogProc.update_visible_n(dogid);

    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    return "redirect:/dog/list_search";  // /templates/dog/msg.html
  }
}
