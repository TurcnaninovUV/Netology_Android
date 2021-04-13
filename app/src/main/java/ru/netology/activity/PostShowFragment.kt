package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.activity.NewAndEditPostFragment.Companion.textArg
import ru.netology.databinding.FragmentPostShowBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.viewmodel.PostViewModel

class PostShowFragment : Fragment() {

    companion object {
        var Bundle.showPost: Post? by PostArg
    }

    private val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostShowBinding.inflate(
                inflater,
                container,
                false
        )

        arguments?.showPost.let {
            val postShow = it
            with(binding) {
                author.text = postShow?.author!!
                published.text = postShow.published
                content.text = postShow.content
                like.text = viewModel.reduction(postShow.likes)
                repost.text = viewModel.reduction(postShow.repost)
                like.isChecked = postShow.likedByMe
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(postShow.id)
                                    findNavController().navigate(R.id.action_postShowFragment2_to_feedFragment)
                                    true
                                }
                                R.id.edit -> {
                                    findNavController().navigate(R.id.action_postShowFragment2_to_newAndEditPostFragment,
                                            Bundle().apply { textArg = postShow.content })
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
                like.setOnClickListener {
                    viewModel.likeById(postShow.id)
                }
                repost.setOnClickListener {
                    viewModel.repost(postShow.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, postShow.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }
                play.setOnClickListener {
                    val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(postShow.video))
                    startActivity(intentVideo)
                }
                videoImage.setOnClickListener {
                    val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(postShow.video))
                    startActivity(intentVideo)
                }
                if (postShow.video != null) groupVideo.visibility =
                        View.VISIBLE else groupVideo.visibility = View.GONE
            }
        }
        return binding.root
    }
}







