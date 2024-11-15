package com.example.btl.btl.repositories;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.btl.btl.models.Shoe;

import org.springframework.stereotype.Repository;

@Repository
public interface ShoeRepo extends JpaRepository<Shoe, Integer> {
    @Query(value = "SELECT * FROM shoes s WHERE status = 1 ORDER BY (1 - (s.promote_price / s.price)) DESC  LIMIT :n", nativeQuery = true)
    List<Shoe> findMostPromoteShoes(@Param("n") int n);

    @Query(value = "SELECT * FROM shoes WHERE status = 1 ORDER BY created_at DESC LIMIT :n", nativeQuery = true)
    List<Shoe> findTopNewestShoes(@Param("n") int n);

    @Query(value = "SELECT * FROM shoes WHERE status = 1 ORDER BY hot_order DESC LIMIT :n", nativeQuery = true)
    List<Shoe> findTopHotestShoes(@Param("n") int n);

    @Query(value = "SELECT * FROM shoes", nativeQuery = true)
    List<Shoe> findAllShoes();

    @Query(value = "SELECT shoes.* "
            + "FROM web.order_details "
            + "INNER JOIN web.shoes ON order_details.shoe_id = shoes.id "
            + "WHERE status = 1 "
            + "GROUP BY shoes.id, shoes.title "
            + "ORDER BY SUM(order_details.quantity) DESC", nativeQuery = true)
    List<Shoe> findMostBuyShoes(int n);

    Page<Shoe> findByCategoryIdAndStatus(int categoryId, int status, Pageable pageable);

    @Query("SELECT s FROM Shoe s JOIN Category c ON s.categoryId = c.id WHERE s.status = 1 AND ((LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%'))) OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))))")
    Page<Shoe> search(@Param("query") String query, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Shoe s SET s.status = :status, s.lastUpdatedBy = :lastUpdatedBy, s.updatedAt = :updatedAt WHERE s.id = :id")
    int updateStatus(@Param("id") int id, @Param("status") int status,
            @Param("lastUpdatedBy") String lastUpdatedBy,
            @Param("updatedAt") Timestamp updatedAt);
}