package com.zephyr.repository;

import com.zephyr.domain.Rol;

import java.util.List;

public interface RolRepository {
    List<Rol> findAll();
}
