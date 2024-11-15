package com.example.btl.btl.services;

import com.example.btl.btl.dtos.CartItem;
import com.example.btl.btl.dtos.ShoeFullDetail;
import com.example.btl.btl.models.Category;
import com.example.btl.btl.models.Shoe;
import com.example.btl.btl.models.ShoeDetail;
import com.example.btl.btl.repositories.CategoryRepo;
import com.example.btl.btl.repositories.ShoeDetailRepo;
import com.example.btl.btl.repositories.ShoeRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoeService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ShoeRepo shoeRepo;

    @Autowired
    private ShoeDetailRepo shoeDetailRepo;

    public Page<Shoe> getAllShoes(Pageable pageable) {
        return shoeRepo.findAll(pageable);
    }

    public ShoeFullDetail getShoeById(int shoeId) {
        Shoe shoe = shoeRepo.findById(shoeId).orElse(null);
        if (shoe == null) {
            return null;
        }
        List<ShoeDetail> shoeDetails = shoeDetailRepo.findByShoeId(shoeId);
        return new ShoeFullDetail(shoe, shoeDetails);
    }

    public void addShoe(ShoeFullDetail shoeFullDetail) {
        Shoe shoe = shoeFullDetail.getShoe();
        List<ShoeDetail> shoeDetails = shoeFullDetail.getShoeDetails();
        shoeRepo.save(shoe);
        for (ShoeDetail shoeDetail : shoeDetails) {
            shoeDetail.setShoeId(shoe.getId());
            shoeDetailRepo.save(shoeDetail);
        }
    }

    public void updateShoe(ShoeFullDetail shoeFullDetail) throws Exception {
        Shoe newShoe = shoeFullDetail.getShoe();
        List<ShoeDetail> shoeDetails = shoeFullDetail.getShoeDetails();
        Shoe oldShoe = shoeRepo.findById(newShoe.getId()).orElse(null);
        if (oldShoe == null)
            throw new Exception("No shoe has ID: " + newShoe.getId());
        oldShoe.setCategoryId(newShoe.getCategoryId());
        oldShoe.setTitle(newShoe.getTitle());
        oldShoe.setPrice(newShoe.getPrice());
        oldShoe.setPromotePrice(newShoe.getPromotePrice());
        oldShoe.setImage(newShoe.getImage() != null ? newShoe.getImage() : oldShoe.getImage());
        oldShoe.setDescription(newShoe.getDescription());
        oldShoe.setUpdatedAt(newShoe.getUpdatedAt());
        oldShoe.setLastUpdatedBy(newShoe.getLastUpdatedBy());
        oldShoe.setHotOrder(newShoe.getHotOrder());

        shoeRepo.save(oldShoe);
        shoeDetailRepo.deleteAllByShoeId(oldShoe.getId());
        for (ShoeDetail shoeDetail : shoeDetails) {
            shoeDetail.setShoeId(oldShoe.getId());
            shoeDetailRepo.save(shoeDetail);
        }
    }

    public void deleteShoe(int shoeId) {
        List<ShoeDetail> shoeDetails = shoeDetailRepo.findByShoeId(shoeId);
        if (shoeDetails != null)
            for (ShoeDetail shoeDetail : shoeDetails) {
                shoeDetailRepo.delete(shoeDetail);
            }
        shoeRepo.deleteById(shoeId);
    }

    public List<Shoe> getNewestShoes(int n) {
        return shoeRepo.findTopNewestShoes(n);
    }

    public List<Shoe> getAllShoes() {
        return shoeRepo.findAllShoes();
    }

    public List<Shoe> getHottestShoes(int n) {
        return shoeRepo.findTopHotestShoes(n);
    }

    public List<Shoe> getMostBuy(int n) {
        return shoeRepo.findMostBuyShoes(n);
    }

    public List<Shoe> getMostPromoteShoes(int n) {
        return shoeRepo.findMostPromoteShoes(n);
    }

    public Page<Shoe> getByCategoryURL(String categoryUrl, Pageable pageable) {
        Category category = categoryRepo.findByUrl(categoryUrl);

        if (category == null) {
            return null;
        }

        Page<Shoe> shoes = shoeRepo.findByCategoryIdAndStatus(category.getId(), 1, pageable);
        return shoes;
    }

    public CartItem getCartItemByShoeDetailId(int shoeId) {
        CartItem item = new CartItem();
        ShoeDetail detail = shoeDetailRepo.findById(shoeId).orElse(null);
        Shoe shoe = shoeRepo.findById(detail.getShoeId()).orElse(null);

        item.setShoeId(shoe.getId());
        item.setShoeDetailId(detail.getId());
        item.setImage(shoe.getImage());
        item.setPrice(shoe.getPrice());
        item.setPromotePrice(shoe.getPromotePrice());
        item.setTitle(shoe.getTitle());
        item.setColor(detail.getColor());
        item.setSize(detail.getSize());

        return item;
    }

    public Page<Shoe> search(String query, Pageable pageable) {
        return shoeRepo.search(query, pageable);
    }

    public int changeStatus(int id, int status, String admin_id) {
        return shoeRepo.updateStatus(id, status, admin_id, new Timestamp(System.currentTimeMillis()));
    }
}