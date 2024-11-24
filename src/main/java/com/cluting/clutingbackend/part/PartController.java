package com.cluting.clutingbackend.part;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/part")
@RequiredArgsConstructor
public class PartController {
    private final PartService partService;

    // 그룹 추가하기
}
