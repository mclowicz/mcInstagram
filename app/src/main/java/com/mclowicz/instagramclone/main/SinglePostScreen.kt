package com.mclowicz.instagramclone.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.mclowicz.InstagramViewModel
import com.mclowicz.instagramclone.DestinationScreen
import com.mclowicz.instagramclone.auth.CommonDivider
import com.mclowicz.instagramclone.data.PostData
import com.mclowicz.instagramclone.R

@Composable
fun SinglePostScreen(
    navController: NavController,
    vm: InstagramViewModel,
    post: PostData
) {

    val comments = vm.comments.value

    LaunchedEffect(key1 = Unit, block = {
        vm.getComments(postId = post.postId)
    })

    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Log.e("screen", "singlePost")
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })
            CommonDivider()
            SinglePostDisplay(
                navController = navController,
                vm = vm,
                post = post,
                numberOfComments = comments.size
            )
        }
    }
}

@Composable
fun SinglePostDisplay(
    navController: NavController,
    vm: InstagramViewModel,
    post: PostData,
    numberOfComments: Int
) {
    val userData = vm.userData.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .height(200.dp)
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = post.userImage),
                    contentDescription = null
                )
            }
            Text(text = post.username ?: "")
            Text(text = " . ", modifier = Modifier.padding(8.dp))

            if (userData?.userId == post.userId) {
                Text(text = "Follow", color = Color.Blue, modifier = Modifier.clickable {
                    // Follow a user
                    userData?.userId?.let { vm.onFollowClick(userId = it) }
                })
            } else if (userData?.following?.contains(post.userId) == true) {
                Text(
                    text = "Following",
                    color = Color.Gray,
                    modifier = Modifier.clickable { post.userId?.let { vm.onFollowClick(it) } })
            } else {
                Text(
                    text = "Follow",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        post.userId?.let { userId ->
                            vm.onFollowClick(
                                userId
                            )
                        }
                    })
            }
        }
    }
    Box {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommonImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(text = "${post.likes?.size ?: 0} likes", modifier = Modifier.padding(start = 8.dp))
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(
            text = post.postDescription ?: "", modifier = Modifier
                .padding(start = 8.dp)
        )
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = "$numberOfComments comments",
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    post.postId?.let { postId ->
                        navController.navigate(DestinationScreen.CommentsScreen.createRoute(postId))
                    }
                }
        )
    }
}