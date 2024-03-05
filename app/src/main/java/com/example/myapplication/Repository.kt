package ru.btpit.nmedia
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id:Long)
    fun shareById(id:Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun edit(post: Post)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            header = "Лада Приора",
            content = "Из ныне выпускаемых моделей Лады наиболее часто подвергается  тюнингу Приора, и АвтоВАЗ не преминул воспользоваться желанием покупателей иметь машины «не как у всех». В прошлом году завод выпустил партию Приор с двуцветной окраской кузова, а теперь к выходу на рынок готовы сразу две специальные версии. В исполнении Black Edition седан окрашен в черный цвет «Пантера», а White Edition — в «Белое облако». Оба колера — из штатной палитры модели, но на спецверсиях такой же цвет у 15-дюймовых легкосплавных колес и молдинга на крышке багажника.",
            dataTime = "21 февраля в 19:12",
            isLike = false,
            amountlike = 999,
            amountrepost = 15,
            amountview = 500,
            isRepos = false
        ),
        Post(
            id = nextId++,
            header = "Лада Приора",
            content = "Есть отличия и в салоне: это новая двуцветная обивка сидений с контрастной прострочкой и черные глянцевые вставки на центральной консоли и руле. Изменения, понятное дело, скромные, но для привлечения целевой аудитории создание таких модификаций — верный ход. Черные и белые седаны предлагаются в максимальной на сегодняшний день комплектации с шестнадцатиклапанником 1.6 (106 л.с.), кондиционером, подогревом передних сидений, АБС, подушкой безопасности водителя и электроусилителем руля. Цена — 491 тысяча рублей, то есть доплата, по сравнению с аналогично оснащенной «обычной» Приорой в комплектации Норма, 17 тысяч рублей. Тираж спецверсий не ограничен.",
            dataTime = "27 Февраля в 12:56",
            isLike = false,
            amountlike = 0,
            amountrepost = 0,
            amountview = 0,
            isRepos = false
        ),

        )

    private val data = MutableLiveData(posts)
    private val edited = MutableLiveData(empty)
    override fun get(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                if (it.isLike)
                    it.amountlike--
                else
                    it.amountlike++
                it.copy(isLike = !it.isLike)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else {
                it.amountrepost++
                it.copy(isRepos = !it.isRepos)
            }
        }
        data.value = posts
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id}
        data.value = posts
    }
    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    header = "Me",
                    isLike = false,
                    isRepos = false,
                    dataTime = "now",
                    amountview = 0
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
    override fun edit(post: Post) {
        edited.value = post
    }
}
private val empty = Post(
    id = 0,
    content = "",
    amountview = 0,
    amountlike = 0,
    amountrepost = 0,
    dataTime = "",
    header = "",
    isLike = false,
    isRepos = false
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    val edited = MutableLiveData(empty)
    fun edit(post: Post) {
        edited.value = post
    }
    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }
    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id:Long) = repository.shareById(id)
    fun removeById(id : Long) = repository.removeById(id)
}