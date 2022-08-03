package com.musseukpeople.woorimap.tag.application;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.entity.Tag;
import com.musseukpeople.woorimap.tag.entity.TagRepository;
import com.musseukpeople.woorimap.tag.infrastructure.QueryTagRepository;
import com.musseukpeople.woorimap.couple.domain.Couple;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;


    @Transactional
    public List<Long> createTag(Long coupleId, List<TagRequest> tagRequestList) {

        // 새로 저장해야할 tag list
        List<TagRequest> tagRequestListWithIdOfNull = new ArrayList<>();

        // 이미 저장된 tag list
        List<Long> tagIdList = new ArrayList<>();

        for (TagRequest tr : tagRequestList) {
            if (tr.getId() == null) {
                tagRequestListWithIdOfNull.add(tr);
            } else {
                tagIdList.add(tr.getId());
            }
        }

        List<Tag> tagListFromDb = tagRepository.findExistTagByCoupleId(coupleId, tagRequestListWithIdOfNull);

        // 새로 저장할 tag list와 db의 tag list를 비교해서 중복된 tag name list
        List<TagRequest> tagRequestToRemove = new ArrayList<>();
        for (TagRequest tr : tagRequestListWithIdOfNull) {
            for (Tag tagFromDb : tagListFromDb) {
                if (tagFromDb.getName().equals(tr.getName())) {
                    tagRequestToRemove.add(tr);
                }
            }
        }

        tagRequestListWithIdOfNull.removeAll(tagRequestToRemove);

        List<Tag> tagListToInsert = toTags(tagRequestListWithIdOfNull, coupleId);
        List<Tag> tagListInserted = tagRepository.saveAll(tagListToInsert);

        for (Tag tag : tagListInserted) {
            tagIdList.add(tag.getId());
        }

        return tagIdList;
    }

    @Transactional
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public List<Tag> toTags(List<TagRequest> tagRequestList, Long coupleId) {
        List<Tag> newTags = new ArrayList<>();
        for (TagRequest tr : tagRequestList) {
            newTags.add(new Tag(tr.getName(), tr.getColor(), new Couple(coupleId)));
        }
        return newTags;
    }
}
