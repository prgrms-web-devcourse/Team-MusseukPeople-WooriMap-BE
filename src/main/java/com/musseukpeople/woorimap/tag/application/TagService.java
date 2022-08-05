package com.musseukpeople.woorimap.tag.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.domain.TagRepository;
import com.musseukpeople.woorimap.tag.domain.Tags;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Tags findOrCreateTags(Couple couple, List<TagRequest> tagRequests) {
        Tags requestTags = toTags(couple, tagRequests);
        Tags existTags = new Tags(tagRepository.findExistTagByCoupleId(couple, requestTags.toNames()));

        Tags newTags = requestTags.removeAllByName(existTags);
        tagRepository.saveAll(newTags.getList());

        return existTags.addAll(newTags);
    }

    private Tags toTags(Couple couple, List<TagRequest> tagRequestList) {
        return tagRequestList.stream()
            .map(tagRequest -> new Tag(tagRequest.getName(), tagRequest.getColor(), couple))
            .collect(collectingAndThen(toList(), Tags::new));
    }
}
