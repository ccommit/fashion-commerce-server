package com.ccommit.fashionserver.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.ccommit.fashionserver.dto.request
 * fileName       : RequestCreateOrder
 * author         : juoiy
 * date           : 2023-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-30        juoiy       최초 생성
 */

@Getter
@Setter
public class RequestCreateOrder {
    List<Integer> orderProductIdList;
}
