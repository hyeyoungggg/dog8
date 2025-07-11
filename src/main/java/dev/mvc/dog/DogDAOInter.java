package dev.mvc.dog;

import java.util.ArrayList;
import java.util.Map;

//MyBATIS 기준으로 추상 메소드를 만들면 Spring Boot가 자동으로 class로 구현한다
public interface DogDAOInter<DogVO> {
  /**
   * <pre>
   * MyBATIS: <insert id = "create" parameterType = "dev.mvc.cate.CateVO">
   * insert: INSERT SQL, 처리 후 등록된 레코드 갯수를 리턴
   * id: 자바 메소드명
   * parameterType: MyBATS가 전달받는 VO 객체 타입
   * </pre>
   * @param <DogVO>
   * @param cateVO
   * @return 등록된 레코드 갯수
   */
  public int create(DogVO dogVO);

  /**
   * 전체목록
   * @param <DogVO>
   * @return
   */
  public ArrayList<DogVO> list_all(); //추상메소드

  /**
   * 검색, 전체 목록
   * @return
   */
  public ArrayList<DogVO> list_search(String word);

  /**
   * 조회
   * @param cateno
   * @return
   */
  public DogVO read(int dogid);

  /**
   * 수정
   * @param cateno
   * @return
   */
  public int update(DogVO dogVO);

  /**
   * 삭제 처리
   * delete id="delete" parameterType="Integer"
   * @param int
   * @return 삭제된 레코드 갯수
   */
  public int delete(int dogid);

  public int update_seqno_forward(int dogid);

  public int update_seqno_backward(int dogid);

  public int update_visible_y(int dogid);

  public int update_visible_n(int dogid);

  public ArrayList<DogVO> list_all_grp_y();

  public ArrayList<DogVO> list_all_name_y(String grp);

  public ArrayList<String> grpset();

  public int list_search_count(String word);

  public ArrayList<DogVO> list_search_paging(Map map);
  
  /**
   * 특정 카테고리에 속한 콘텐츠 수 리턴
   * @param cateno
   * @return
   */
  public int count_by_dogid(int dogid);
  
  public int update_cnt_breed_min(Map <String, Object>min);
  
  public int delete_by_dogid(int dogid);
}
