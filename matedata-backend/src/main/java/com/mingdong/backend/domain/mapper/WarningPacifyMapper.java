package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningPacify;

import java.util.List;

public interface WarningPacifyMapper
{
    void add(WarningPacify warningPacify);

    void addAll(List<WarningPacify> list);

    void updateSkipNull(WarningPacify warningPacify);
}
