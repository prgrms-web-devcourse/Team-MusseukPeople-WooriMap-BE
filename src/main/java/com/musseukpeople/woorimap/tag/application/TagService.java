package com.musseukpeople.woorimap.tag.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.tag.domain.Tag;
import com.musseukpeople.woorimap.tag.domain.TagRepository;
import com.musseukpeople.woorimap.couple.domain.Couple;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public List<Long> createTag(Couple couple, List<TagRequest> tagRequestList) {

        // 요청 받은 tag name 리스트가 DB에 저장되어 있는지 확인
        // 만약 DB에 cafe, seoul, korean 으로 저장되어 있고, 요청 받은 tag name 리스트가 cafe, korean 이라면
        // cafe, korean 이 반환 됨. - 저장되어 있는 tag name 리스트를 반환함
        List<Tag> tagListFromDb = tagRepository.findExistTagByCoupleId(couple, getTagNameInTagRequestList(tagRequestList));

        // 저장되어 있는 tag의 id 리스트를 저장
        List<Long> tagIdList = getTagIdFromTagList(tagListFromDb);

        // TagRequest 리스트에서 이미 저장되어 있는 Tag 리스트를 제외함 - 최종적으로 저장할 TagRequest 리스트만 필요하니깐
        List<TagRequest> tagRequestListToSave = deduplicationTagRequest(tagRequestList, tagListFromDb);

        List<Tag> tagListToSave = toTags(couple, tagRequestListToSave);
        List<Tag> tagListInserted = tagRepository.saveAll(tagListToSave);

        for (Tag tag : tagListInserted) {
            tagIdList.add(tag.getId());
        }

        return tagIdList;
    }

    private List<Long> getTagIdFromTagList(List<Tag> tagList) {
        return tagList.stream().map(Tag::getId).collect(Collectors.toList());
    }

    private List<TagRequest> deduplicationTagRequest(List<TagRequest> tagRequestList, List<Tag> tagListFromDb) {
        if (tagListFromDb.isEmpty()) {
            return tagRequestList;
        }

        List<TagRequest> tagRequestToRemove = new ArrayList<>();
        for (TagRequest tr : tagRequestList) {
            for (Tag tagFromDb : tagListFromDb) {
                if (tagFromDb.getName().equals(tr.getName())) {
                    tagRequestToRemove.add(tr);
                }
            }
        }

        if (tagRequestToRemove.isEmpty()) {
            return tagRequestList;
        }

        tagRequestList.removeAll(tagRequestToRemove);

        return tagRequestList;
    }

    private List<String> getTagNameInTagRequestList(List<TagRequest> tagRequestList) {
        return tagRequestList.stream().map(TagRequest::getName).collect(Collectors.toList());
    }

    private List<Tag> toTags(Couple couple, List<TagRequest> tagRequestList) {
        return tagRequestList.stream().map(tr -> new Tag(tr.getName(), tr.getColor(), couple)).collect(Collectors.toList());
    }
}
