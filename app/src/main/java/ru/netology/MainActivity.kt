package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов" +
                        " по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, " +
                        "аналитике и управлению. Мы растем сами и помогаем расти студентам: " +
                        "от новичков до уверенных провессионалов. Но самое важное остается с " +
                        "нами: мы верим, что в каждом есть сила, которая заставляет хотеть больше, " +
                        "целиться выше, бежать быстрее. Наша миссия - помочь вставать на путь роста " +
                        "и начать цеопчку перемен → http://netolo.gy/fyb",
                published = "21 мая в 18:36",
                likedByMe = false,
                likes = 999,
                repost = 564132
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            if (post.likedByMe) {
                like.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            likesCount.text = reduction(post.likes)
            repostCount.text = reduction(post.repost)

            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            like.setOnClickListener {
                Log.d("stuff", "like")
                post.likedByMe = !post.likedByMe
                like.setImageResource(
                        if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                        else R.drawable.ic_baseline_favorite_border_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likesCount.text = reduction(post.likes)
            }

            repost.setOnClickListener {
                Log.d("stuff", "repost")
                post.repost++
                repostCount.text = reduction(post.repost)
            }
        }
    }

    fun reduction(count: Int): String {
        val countReduction = when {
            (count >= 1_000_000) -> "${"%.1f".format(count / 1_000_000.toDouble())}M"
            (count in 1000..9_999) -> "${"%.1f".format(count / 1_000.toDouble())}K"
            (count in 10_000..999_999) -> "${count / 1000}K"
            else -> count
        }.toString()
        return countReduction
    }
}
