package com.motel.mobileproject_motelrental;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";          // tên collection                           string
    public static final String KEY_NAME = "name";                       // tên người dùng                           string
    public static final String KEY_GENDER = "gender";                   // giới tính                                boolean (true là nữ)
    public static final String KEY_BIRTHDAY = "day of birth";           // ngày sinh                                timestamp
    public static final String KEY_HOUSE_NUMBER = "house number";       // số nhà                                   string
    public static final String KEY_WARD = "ward";                       //xã, phường                                string
    public static final String KEY_DISTRICT = "district";               //quận, huyện                               string
    public static final String KEY_CITY = "city";                       // tỉnh, thành phố                          string
    public static final String KEY_EMAIL = "email";                     // email (cũng là tài khoản đăng nhập)      string
    public static final String KEY_PASSWORD = "password";               // mật khẩu                                 string
    public static final String KEY_IMAGE = "image";                     // ảnh đại diện người dùng                  string
    public static final String KEY_PHONE_NUMBER = "phone number";       // số điện thoại                            string
    public static final String KEY_FCM_TOKEN = "fcmtoken";              //token để duy trì đăng nhập                string
    public static final String KEY_AVAILABILITY = "availability";       // trạng thái hoạt động của người dùng      number
    public static final String KEY_STATUS_USER = "status";              // trạng thái của tài khoản                 boolean



    public static final String KEY_COLLECTION_MOTELS = "motels";    // tên collection
    public static final String KEY_POST_AUTHOR = "post author";     // documentID của người dùng đăng trọ           string
    public static final String KEY_TITLE = "title";                 // tiêu đề của bài viết                         string
    public static final String KEY_COUNT_LIKE = "like";             // số lượng like của trọ                        number
    public static final String KEY_COUNT_ROOM = "room";             // số lượng phòng hiện có của trọ               number
    public static final String KEY_MOTEL_NUMBER = "motel number";   // số nhà của trọ                               string
    public static final String KEY_WARD_MOTEL = "ward";              //xã, phường                                   string
    public static final String KEY_DISTRICT_MOTEL = "district";      //quận, huyện                                  string
    public static final String KEY_CITY_MOTEL = "city";             // tỉnh, thành phố                              string
    public static final String KEY_PRICE = "price";                 // giá thuê trọ                                 number
    public static final String KEY_ACREAGE = "acreage";             //diện tích phòng trọ                           number
    public static final String KEY_CHARACTERISTIC = "characteristic";//đặc điểm của trọ                             string
    public static final String KEY_DESCRIPTION = "description";     // mô tả                                        string
    public static final String KEY_COUNT_BATH = "bathroom";         //số lượng phòng tắm                            number
    public static final String KEY_COUNT_BED = "bedroom";           // số lượng phòng ngủ                           number
    public static final String KEY_COUNT_FRIDGE = "fridge";         // số lượng tủ lạnh                             number
    public static final String KEY_COUNT_WASHING_MACHINE = "washing machine";  // số lượng máy giặt                 number
    public static final String KEY_GARET = "garet";                 // có gác xếp không                             boolean
    public static final String KEY_PRICE_WIFI = "wifi";             // giá tiền wifi                                number
    public static final String KEY_PRICE_PARKING = "parking";       // giá tiền đỗ xe                               number
    public static final String KEY_DEPOSIT_MONEY = "deposit";       // tiền đặt cọc                                 number
    public static final String KEY_START_TIME = "start time";       // thời gian mở cửa                             string
    public static final String KEY_END_TIME = "end time";           // thòi gian đóng của                           string
    public static final String KEY_TYPE_ID = "typeID";              //documentid của loại trọ                       string
    public static final String KEY_STATUS_MOTEL = "status";         //trạng thái của bài viết đăng trọ              boolean
    public static final String KEY_IMAGE_LIST = "image list"; // hình ảnh của trọ                                   array<string>


    public static final String KEY_COLLECTION_TYPES = "types"; // tên collection
    public static final String KEY_NAME_TYPE = "type";          // tên loại trọ (trọ, chung cư, nhà nguyên căn)     string

    public static final String KEY_COLLECTION_COMMENTS = "comments"; // tên collection
    public static final String KEY_COMMENTER = "commenter";         // documentid của người bình luận               string
    public static final String KEY_COMMENT_MOTEL = "comment motel"; //documentid của trọ được đăng bài              string
    public static final String KEY_TIME_COMMENT = "time comment";   // thòi gian bình luận                          timestamp
    public static final String KEY_CONTENT_COMMENT = "content comment"; // nội dung của bình luận                   string

    public static final String KEY_COLLECTION_LIKES = "likes";  // tên collection
    public static final String KEY_POST_LIKER = "post liker";   //documentid của người like                         string
    public static final String KEY_LIKED_MOTEL = "liked motel"; //documentid của trọ                                string
    public static final String KEY_TIME_LIKE = "time like";     // thời gian like                                   timestamp


}
