# BookNest

## Project Overview

### 1. Short Description
**BookNest** is a user-friendly book tracking and review application that allows individuals to manage their personal book collections, browse for new books, and interact with a community of readers by viewing and commenting on book reviews. Users can create a personal library by adding new books, updating reading statuses (e.g., "To Read," "Reading," "Finished"), and removing books they no longer wish to track. Additionally, users can explore reviews written by others, leave comments, and engage in discussions. Users can manage their book collections even when offline, including viewing, adding, and updating book information, and the app will sync data when back online. However, delete operations require an internet connection.

### 2. Domain Details
In **BookNest**, the primary entities that will be persisted include **Book**, **User**, **Review**, and **Comment**. Below are the fields for each entity:

#### Book
- **ID**: A unique identifier automatically generated for each book.
- **Title**: The name of the book.
- **Author**: The author of the book.
- **Description**: A brief description of the book.
- **Status**: Indicates whether the book is "To Read," "Reading," or "Finished."
- **User Rating**: A user-defined rating out of 5, reflecting individual enjoyment of the book.
- **Overall Rating**: The average rating of the book, calculated based on ratings submitted by all users.
- **Number of Votes**: The total count of users who have rated the book, providing context for the overall rating.
- **Review**: A text field for users to write their personal thoughts or reviews about the book.
- **Image URL**: A link to an image of the book's cover, allowing users to visually identify the book in their library.

#### User
- **User ID**: A unique identifier for each user.
- **Username**: A unique name chosen by the user.
- **Email**: The user's email address for login and notifications.
- **Password**: A password used for user authentication.
- **Books**: A list of books that the user has in their library (each associated with the Book entity).

#### Review
- **Review ID**: A unique identifier for each review.
- **Book**: The associated book being reviewed (linked to the Book entity).
- **User**: The user who wrote the review (linked to the User entity).
- **Review Content**: The text of the review written by the user about the book.
- **Rating**: The user's rating for the book (1-5 stars).
- **Comments**: A list of comments on the review (linked to the Comment entity).
- **Timestamp**: The date and time when the review was posted.

#### Comment
- **Comment ID**: A unique identifier for each comment.
- **Review**: The review being commented on (linked to the Review entity).
- **User**: The user who made the comment (linked to the User entity).
- **Comment Content**: The text of the comment.
- **Timestamp**: The date and time when the comment was made.

### 3. CRUD Operations
Here’s how each CRUD operation applies to **Book**, **User**, **Review**, and **Comment** entities:

#### Books CRUD Operations
- **Create**: Users can add new books to their library by providing the book’s title, author, and description. The book will be saved locally and synced to the server when online.
- **Read**: Users can browse books in their library or explore new books added by others. If offline, the app displays locally stored book data and notifies users that real-time updates are unavailable.
- **Update**: Users can update book details, including reading status, review, and rating. The updates can be made offline and will be synced with the server once the device reconnects.
- **Delete**: Users can delete books from their library. Deletions cannot be made offline; users will receive a notification that the operation is unavailable.

#### Users CRUD Operations
- **Create**: Users can create an account by providing a username, email, and password. The account is stored both locally and on the server.
- **Read**: Users can view their profile, including their book collection and interactions with reviews. When offline, local profile data is accessible.
- **Update**: Users can update their profile details such as username or password. Updates can be made offline and will sync when online.
- **Delete**: Users can delete their account, which requires an internet connection. An offline notification will inform them that this action is unavailable.

#### Reviews CRUD Operations
- **Create**: Users can create a new review by writing about a book in their library and rating it. Reviews made offline will be stored locally and synchronized with the server once the device reconnects.
- **Read**: Users can browse through reviews written by others, read comments, and interact with the community. Offline, users can view locally stored reviews, with a message indicating the limitations.
- **Update**: Users can update their own reviews, such as editing review content or rating. Updates can be made offline and will be synced once online.
- **Delete**: Users can delete their reviews. This requires being online, and offline deletions cannot be made; users will receive a notification that the operation is unavailable.

#### Comments CRUD Operations
- **Create**: Users can add comments on reviews written by others. Comments made offline will be stored locally and synced to the server once the app reconnects.
- **Read**: Users can read comments on reviews. Offline, only locally stored comments will be available.
- **Update**: Users can edit their own comments. Updates can be made offline and will sync when the device reconnects.
- **Delete**: Users can delete their comments. Deletion requires an internet connection; users will receive a notification that the operation is unavailable.

## 4. Persistence Details
The **BookNest** application ensures data persistence through both local storage (for offline access) and server storage (for synchronization across devices and users). Below is a detailed breakdown of what is persisted for each feature:

### Local Persistence
1. **Books**:
   - **Persisted**: Existing and new books are stored locally, allowing users to add and view their library offline.
   - **Not Persisted**: Changes made while offline (e.g., adding new books) are not reflected on the server until synchronization occurs when the device is back online.

2. **Users**:
   - **Persisted**: User accounts and profile details (e.g., username, email) are saved locally for quick access and functionality without an internet connection.
   - **Not Persisted**: New account creations are only saved locally until the user is online, at which point they are sent to the server.

3. **Reviews**:
   - **Persisted**: Existing reviews are stored locally. Reviews created offline are saved temporarily, allowing users to write new reviews without an internet connection.
   - **Not Persisted**: Reviews created offline will not be visible to other users or synced to the server until the app reconnects.

4. **Comments**:
   - **Persisted**: Existing comments are stored locally. Comments added to reviews while offline are temporarily saved.
   - **Not Persisted**: Comments made offline are not synced to the server until the device reconnects, meaning they won't be visible to other users until then.

### Server Persistence
1. **Books**:
   - **Persisted**: Changes to books are synced to the server when the app is online, ensuring that the global library remains up-to-date for all users.
   - **Not Persisted**: Users cannot delete books while offline; these actions require an internet connection to be saved on the server.

2. **Users**:
   - **Persisted**: User account creation and updates to profile information are sent to the server for storage, ensuring data consistency across devices.
   - **Not Persisted**: Users cannot delete profiles while offline; these actions require an internet connection to be saved on the server.

3. **Reviews**:
   - **Persisted**: Reviews written while offline are synced to the server once the user is back online, allowing others to see the latest content.
   - **Not Persisted**: Users cannot delete reviews while offline; these actions require an internet connection to be saved on the server.

4. **Comments**:
   - **Persisted**: Comments made on reviews offline are stored locally and synced with the server once the device is online, keeping comment threads active.
   - **Not Persisted**: Users cannot delete comments while offline; these actions require an internet connection to be saved on the server.

## 5. Offline Scenarios
**BookNest** supports offline usage with specific scenarios for each CRUD operation:

### All entities Offline Scenarios
- **Create (Offline)**: Users can add new entities while offline. These will be saved locally and synced with the server when the device reconnects.
- **Read (Offline)**: Users can view entities. If the app is offline, it will notify users that server data isn’t available.
- **Update (Offline)**: Users can update entities while offline. Changes will sync with the server when back online.
- **Delete (Offline)**: Users cannot delete entities while offline. A notification will inform them that deletion requires an online connection.
