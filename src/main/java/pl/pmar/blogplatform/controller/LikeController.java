package pl.pmar.blogplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pmar.blogplatform.model.entity.Like;
import pl.pmar.blogplatform.service.LikeService;

@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }


    @PostMapping("/likes/")
    public ResponseEntity<Like> createLike(@RequestParam Integer postId) {
        return likeService.createLike(postId);
    }

    @DeleteMapping("/likes/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Integer id) {
        return likeService.deleteLike(id);
    }
}
