package com.example.btl.btl.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.btl.btl.models.ShoeDetail;

import org.springframework.stereotype.Repository;

@Repository
public interface ShoeDetailRepo extends JpaRepository<ShoeDetail, Integer> {
    List<ShoeDetail> findByShoeId(int shoeId);
    @Transactional
    void deleteAllByShoeId(@Param("shoeId") int shoeId);
    ShoeDetail findByShoeIdAndColorAndSize(int shoeId, String color, int size);
}