package com.ohdaesan.shallwepets.bookmark.controller;

import com.ohdaesan.shallwepets.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bookmark")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;


}
