package dev.mvc.dog;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE Dogs (
//dogid NUMBER(10) NOT NULL PRIMARY KEY,  -- 강아지 ID (Primary Key)
//name VARCHAR2(100) NOT NULL,             -- 강아지 이름
//breed VARCHAR2(100) NOT NULL,            -- 품종
//content VARCHAR2(500),               -- 품종 설명
//rdate  Date    NOT NULL,  -- 생성일
//SEQNO  NUMBER(5)     DEFAULT 1     NOT NULL
//);

@Setter @Getter @ToString
public class DogVO {
  /** 강아지 ID, Sequence에서 자동 생성 */
  private Integer dogid;

  /** 강아지 이름 */
  @NotEmpty(message="강아지 이름은 필수 항목입니다.")
  @Size(min=2, max=100, message="강아지 이름은 최소 2자에서 최대 100자입니다.")
  private String name;
  
  /** 관련 자료수 */
  @NotNull(message = "관련 자료수는 필수 입력 항목입니다.")
  @Min(value = 0)
  @Max(value = 1000000)
  private Integer cnt = 0;

  /** 품종 */
  @NotEmpty(message="품종 입력은 필수 항목입니다.")
  @Size(min=2, max=100, message="품종 이름은 최소 2자에서 최대 100자입니다.")
  private String breed;

  /** 품종 설명 */
  @Size(max=500, message="품종 설명은 최대 500자까지 입력 가능합니다.")
  private String content;

  /** 출력 순서 */
  @NotNull(message="출력 순서는 필수 입력 항목입니다.")
  @Min(value=1)
  @Max(value=1000000)
  private Integer seqno = 1;

  /** 출력 모드 */
  @NotEmpty(message="출력 모드는 필수 항목입니다.")
  @Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
  private String visible = "N";

  /** 수정일 (자동 생성) */
  private String rdate;
  
  private int replycnt;

}
