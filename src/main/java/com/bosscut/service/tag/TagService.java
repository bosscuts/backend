package com.bosscut.service.tag;

import com.bosscut.domain.Tag;

import java.util.List;

public interface TagService {

    public Tag add(Tag tag);

    public List<Tag> findAll();
}
