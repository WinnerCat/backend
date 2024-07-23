package com.winner_cat.domain.scrap.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 게시글 아이디, 제목, 태그들
 */
@Data
@Builder
public class ScrapPreviewResponseDTO {
    private Long articleId;
    private String title;
    private List<String> tags;
}
