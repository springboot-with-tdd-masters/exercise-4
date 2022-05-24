package com.softvision.library.tdd;

import com.softvision.library.tdd.model.Author;
import com.softvision.library.tdd.model.Book;
import com.softvision.library.tdd.model.User;

public class LibraryMocks {
    public static final long MOCK_BOOK_ID_1 = 1L;
    public static final String MOCK_TITLE_AOW = "The Art Of War";

    public static final String MOCK_AUTHOR_ST = "Sun Tzu";

    public static final long MOCK_BOOK_ID_2 = 2L;
    public static final String MOCK_TITLE_TP = "The Prince";

    public static final String MOCK_AUTHOR_NM = "Niccolo Machiavelli";

    public static final String MOCK_USER1_USERNAME = "mhatch";
    public static final String MOCK_USER1_PASSWORD = "hello123";
    public static final String MOCK_USER1_ROLE = "admin";
    public static final String MOCK_USER2_USERNAME = "sdiamon";
    public static final String MOCK_USER2_PASSWORD = "welcome123";
    public static final String MOCK_USER2_ROLE = "user";
    private LibraryMocks() {}

    public static Book getMockBook1() {
        return new Book(MOCK_TITLE_AOW);
    }

    public static Book getMockBook2() {
        return new Book(MOCK_TITLE_TP);
    }

    public static Author getMockAuthor1() {
        return new Author(MOCK_AUTHOR_ST);
    }

    public static Author getMockAuthor2() {
        return new Author(MOCK_AUTHOR_NM);
    }

    public static User getMockUser1() {
        return new User(MOCK_USER1_USERNAME, MOCK_USER1_PASSWORD, MOCK_USER1_ROLE);
    }

    public static User getMockUser2() {
        return new User(MOCK_USER2_USERNAME, MOCK_USER2_PASSWORD, MOCK_USER2_ROLE);
    }
}
