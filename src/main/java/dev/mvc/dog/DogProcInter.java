package dev.mvc.dog;

import java.util.ArrayList;
import java.util.Map;

public interface DogProcInter {
  /**
   * 등록
   * @param cateVO
   * @return
   */
  public int create(DogVO cateVO);
  /**
   * 전체목록
   * @return
   */
  public ArrayList<DogVO> list_all(); //추상메소드

  /**
   * 검색, 전체 목록
   * @return
   */
  public ArrayList<DogVO> list_search(String word);

  public DogVO read(int dogid);


  public int update(DogVO dogVo);

  public int delete(int dogid);

  public int update_seqno_forward(int dogid);

  public int update_seqno_backward(int dogid);

  public int update_visible_y(int dogid);

  public int update_visible_n(int dogid);

  public ArrayList<DogVO> list_all_grp_y();

  public ArrayList<DogVO> list_all_name_y(String grp);

  public ArrayList<String> grpset();

  public int list_search_count(String word);

  /**
   * 화면 상단 메뉴
   * @return
   */
  public ArrayList<DogVOMenu> menu();

  /**
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.cate.CateVO" parameterType="Map"
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<DogVO> list_search_paging(String word, int now_page, int record_per_page);

  /**
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음]
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_url 페이지 버튼 클릭시 이동할 주소, @GetMapping(value="/list_search")
   * @param search_count 검색 레코드수
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_url, int search_count, int record_per_page,
      int page_per_block);
  
  /**
   * 특정 카테고리에 속한 콘텐츠 수 리턴
   * @param cateno
   * @return
   */
  public int count_by_dogid(int dogid);
  
  public int update_cnt_breed_min(Map <String, Object>min);
  
  public int delete_by_dogid(int dogid);
}
