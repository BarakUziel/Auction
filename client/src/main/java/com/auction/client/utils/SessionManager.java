package com.auction.client.utils;

import com.auction.client.dtos.ItemDto;
import com.auction.client.dtos.UserDto;

public class SessionManager {

    private static UserDto loggedInUser;
    private static Integer selectedCategoryId;
    private static ItemDto selectedItem;
    private static String selectedCategoryName;

    public static void setSelectedItem(ItemDto item) {
        selectedItem = item;
    }

    public static ItemDto getSelectedItem() {
        return selectedItem;
    }

    public static void login(UserDto user) {
        loggedInUser = user;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static UserDto getLoggedInUser() {
        return loggedInUser;
    }

    public static Integer getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public static String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    public static void setSelectedCategoryId(Integer categoryId) {
        selectedCategoryId = categoryId;
    }

    public static void setSelectedCategoryName(String categoryName) {
        selectedCategoryName = categoryName;
    }
}
