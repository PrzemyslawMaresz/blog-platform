package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.repository.LikeRepository;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }


}
