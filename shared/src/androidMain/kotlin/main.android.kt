import androidx.compose.runtime.Composable

@Composable fun MainView() = App()

actual fun getPatformType(): PlatformType {
    return PlatformType.ANDROID
}