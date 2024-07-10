document.addEventListener('DOMContentLoaded', function() {
    // 검색 기능 구현
    const searchInput = document.querySelector('.search-bar input');
    const searchButton = document.querySelector('.search-bar button');

    searchButton.addEventListener('click', function() {
        const searchQuery = searchInput.value.trim();
        if (searchQuery) {
            // 검색 쿼리를 서버로 전송하고 결과를 렌더링하는 로직 구현
            console.log('Search for:', searchQuery);
        }
    });

    // 포스트 데이터 가져오기 및 렌더링
    fetchPosts();
});

function fetchPosts() {
    // 서버에서 포스트 데이터를 가져오는 로직 구현
    // 예시 데이터
    const posts = [
        {
            title: 'Post Title 1',
            description: 'Brief description of the post...',
            author: 'John Doe',
            date: '2024-06-15',
            views: 50,
            imageUrl: 'placeholder-image.jpg'
        },
        // 더 많은 포스트 데이터 추가
    ];

    const postList = document.querySelector('.post-list');
    posts.forEach(post => {
        const postCard = createPostCard(post);
        postList.appendChild(postCard);
    });
}

function createPostCard(post) {
    const postCard = document.createElement('div');
    postCard.classList.add('post-card');

    const postImage = document.createElement('div');
    postImage.classList.add('post-image');
    const img = document.createElement('img');
    img.src = post.imageUrl;
    img.alt = 'Post Image';
    postImage.appendChild(img);

    const postContent = document.createElement('div');
    postContent.classList.add('post-content');
    const title = document.createElement('h4');
    title.textContent = post.title;
    const description = document.createElement('p');
    description.textContent = post.description;
    const details = document.createElement('div');
    details.classList.add('post-details');
    const author = document.createElement('span');
    author.classList.add('author');
    author.textContent = `by ${post.author}`;
    const date = document.createElement('span');
    date.classList.add('date');
    date.textContent = post.date;
    const views = document.createElement('span');
    views.classList.add('views');
    views.textContent = `${post.views} views`;

    details.appendChild(author);
    details.appendChild(date);
    details.appendChild(views);
    postContent.appendChild(title);
    postContent.appendChild(description);
    postContent.appendChild(details);

    postCard.appendChild(postImage);
    postCard.appendChild(postContent);

    return postCard;
}