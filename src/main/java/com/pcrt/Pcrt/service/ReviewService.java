package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.dto.request.CreateReviewRequest;
import com.pcrt.Pcrt.entities.Review;
import com.pcrt.Pcrt.repository.ReviewRepository;
import com.pcrt.Pcrt.repository.query.ReviewRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewRepositoryQuery reviewRepositoryQuery;

    //create review
    public Review createReview(CreateReviewRequest request) {
        Review review = new Review();
        review.setCustomer(customerService.getCustomerById(request.getCustomerId()));
        review.setStaff(userService.getCurrentUser());
        review.setStaffEvaluate(request.getStaffEvaluate());
        review.setCreatedDate(LocalDate.now());
        review.setStatus("pending");
        return reviewRepository.save(review);
    }

    public List<Review> reviewList() {
        return reviewRepositoryQuery.reviewList();
    }

    public Review getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(RuntimeException::new);
    }

    public Review updateReview(int reviewId, Map<String, String> params) {

        Review review = getReviewById(reviewId);

        //mev = manager evaluate value
        String managerEvaluate = params.get("mev");
        if (managerEvaluate != null && !managerEvaluate.isEmpty()) {

            if (!managerEvaluate.equals("cancel")) {
                review.setManager(userService.getCurrentUser());
                review.setManagerEvaluate(managerEvaluate);
            } else {
                review.setManager(null);
                review.setManagerEvaluate(null);
            }
            reviewRepository.save(review);
            System.out.println("update review success !!");
        } else {

            System.out.println("Lỗi không tìm thấy params để set manager evaluate");
        }
        return review;
    }

    public List<Review> reviewList(String evaluate) {
        return reviewRepositoryQuery.reviewList(evaluate);
    }


}
