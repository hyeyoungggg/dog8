package dev.mvc.dog;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

//- 메뉴의 구성
//여행         까페         영화             <- 카테고리 그룹(대분류)
//└ 춘천    └ 강화도   └ 드라마       <- 카테고리(중분류)
//└ 가평    └ 김포      └ 크리스마스
//└ 속초    └ 영종도   └ SF

@Getter@Setter
public class DogVOMenu {
  /**
   * 카테고리 그룹(대분류)
   */
  private String breed;

  /**
   * 카테고리(중분류)
   */
  private ArrayList<DogVO> list_name;
}
