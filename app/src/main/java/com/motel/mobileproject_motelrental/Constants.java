package com.motel.mobileproject_motelrental;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";          // tên collection                           string
    public static final String KEY_NAME = "name";                       // tên người dùng                           string
    public static final String KEY_GENDER = "gender";                   // giới tính                                boolean true là nữ
    public static final String KEY_BIRTHDAY = "day of birth";           // ngày sinh                                timestamp
    public static final String KEY_HOUSE_NUMBER = "house number";       // số nhà                                   string
    public static final String KEY_WARD = "ward";                       //xã, phường                                 string
    public static final String KEY_DISTRICT = "district";               //quận, huyện
    public static final String KEY_CITY = "city";                       // tỉnh, thành phố
    public static final String KEY_EMAIL = "email";                     // email (cũng là tài khoản đăng nhập)      tring
    public static final String KEY_PASSWORD = "password";               // mật khẩu
    public static final String KEY_IMAGE = "image";                     // ảnh đại diện người dùng
    public static final String KEY_PHONE_NUMBER = "phone number";       // số điện thoại
    public static final String KEY_FCM_TOKEN = "fcmtoken";              //token để duy trì đăng nhập
    public static final String KEY_AVAILABILITY = "availability";       // trạng thái hoạt động của người dùng
    public static final String KEY_STATUS_USER = "status";              // trạng thái cuủa tài khoản



    public static final String KEY_COLLECTION_MOTELS = "motels";    // tên collection
    public static final String KEY_POST_AUTHOR = "post author";     // documentID của người dùng đăng trọ
    public static final String KEY_TITLE = "title";                 // tiêu đề của bài viết
    public static final String KEY_COUNT_LIKE = "like";             // số lượng like của trọ
    public static final String KEY_COUNT_ROOM = "room";             // số lượng phòng hiện có của trọ
    public static final String KEY_MOTEL_NUMBER = "motel number";   // số nhà của trọ
    public static final String KEY_WARD_MOTEL = "ward";              //xã, phường                                 
    public static final String KEY_DISTRICT_MOTEL = "district";      //quận, huyện
    public static final String KEY_CITY_MOTEL = "city";             // tỉnh, thành phố
    public static final String KEY_PRICE = "price";                 // giá thuê trọ
    public static final String KEY_ACREAGE = "acreage";             //diện tích phòng trọ
    public static final String KEY_CHARACTERISTIC = "characteristic";//đặc điểm của trọ
    public static final String KEY_DESCRIPTION = "description";     // mô tả
    public static final String KEY_COUNT_BATH = "bathroom";         //số lượng phòng tắm
    public static final String KEY_COUNT_BED = "bedroom";           // số lượng phòng ngủ
    public static final String KEY_COUNT_FRIDGE = "fridge";         // số lượng tủ lạnh
    public static final String KEY_COUNT_WASHING_MACHINE = "washing machine";  // số lượng máy giặt
    public static final String KEY_GARET = "garet";                 // có gác xếp không
    public static final String KEY_PRICE_WIFI = "wifi";             // giá tiền wifi
    public static final String KEY_PRICE_PARKING = "parking";       // giá tiền đỗ xe
    public static final String KEY_DEPOSIT_MONEY = "deposit";       // tiền đặt cọc
    public static final String KEY_START_TIME = "start time";       // thời gian mở cửa
    public static final String KEY_END_TIME = "end time";           // thòi gian đóng của
    public static final String KEY_TYPE_ID = "typeID";              //documentid của loại trọ
    public static final String KEY_STATUS_MOTEL = "status";         //trạng thái của bài viết đăng trọ

    public static final String KEY_COLLECTION_TYPES = "types"; // tên collection
    public static final String KEY_NAME_TYPE = "type";          // tên loại trọ (trọ, chung cư, nhà nguyên căn)

    public static final String KEY_COLLECTION_COMMENTS = "comments"; // tên collection
    public static final String KEY_COMMENTER = "commenter";         // documentid của người bình luận
    public static final String KEY_COMMENT_MOTEL = "comment motel"; //documentid của trọ được đăng bài
    public static final String KEY_TIME_COMMENT = "time comment";   // thòi gian bình luận
    public static final String KEY_CONTENT_COMMENT = "content comment"; // nội dung của bình luận

    public static final String KEY_COLLECTION_LIKES = "likes";  // tên collection
    public static final String KEY_POST_LIKER = "post liker";   //documentid của người like
    public static final String KEY_LIKED_MOTEL = "liked motel"; //documentid của trọ
    public static final String KEY_TIME_LIKE = "time like";     // thời gian like

    public static final String KEY_COLLECTION_IMAGE = "images"; // tên collection
    public static final String KEY_MOTEL_ID = "motelID";        //documentid của trọ
    public static final String KEY_IMAGE_MOTEL = "image motel"; // hình ảnh của trọ

}
