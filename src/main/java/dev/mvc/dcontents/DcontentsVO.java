package dev.mvc.dcontents;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@ToString
public class DcontentsVO {
  
  public DcontentsVO() { }
  
    /** 컨텐츠 번호 */
    private int dogcontno;

    /** 사용자 번호 (Users 테이블 참조) */
    private int usersno;

    /** 강아지 ID (Dogs 테이블 참조) */
    private int dogid;

    /** 견종 이름 (사용자 입력) */
    private String breed_cont = "";

    /** 견종 이름 */
    private String  dogname = "";
    
    /** 견종 나이 */
    private String  dogage = "";

    /** 크기 (소형, 중형, 대형) */
    private String dog_size = "";

    /** 성격 */
    private String personality = "";

    /** 추천 수 */
    private int recom = 0;

    /** 조회 수 */
    private int cnt = 0;

    /** 댓글 수 */
    private int replycnt = 0;

    /** 비밀번호 (수정/삭제용) */
    private String passwd = "";

    /** 검색어 */
    private String word = "";

    /** 등록일 */
    private String rdate = "";

    /** 지도 정보 */
    private String map = "";

    /** 유튜브 영상 링크 */
    private String youtube = "";

    /** mp4 영상 파일명 */
    private String mp4 = "";

    /** 출력 여부 ('Y', 'N') */
    private String visible = "Y";

    // 업로드 파일 처리용 -------------------------
    
    /** 업로드 이미지 (폼에서 전달되는 이미지 파일) */
    private MultipartFile file1MF = null;

    /** 실제 업로드된 파일명 */
    private String file1 = "";

    /** 서버에 저장된 파일명 */
    private String file1saved = "";

    /** 썸네일 파일명 */
    private String thumb1 = "";

    /** 업로드 파일 크기 (바이트 단위) */
    private long size1 = 0;

    /** 파일 크기 문자열 (ex. 1.3 MB) */
    private String size1_label = "";
  }

