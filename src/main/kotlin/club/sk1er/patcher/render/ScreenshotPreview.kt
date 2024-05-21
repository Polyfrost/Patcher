package club.sk1er.patcher.render

import club.sk1er.patcher.config.PatcherConfig
import cc.polyfrost.oneconfig.libs.elementa.ElementaVersion
import cc.polyfrost.oneconfig.libs.elementa.UIComponent
import cc.polyfrost.oneconfig.libs.elementa.components.UIImage
import cc.polyfrost.oneconfig.libs.elementa.components.Window
import cc.polyfrost.oneconfig.libs.elementa.constraints.RelativeConstraint
import cc.polyfrost.oneconfig.libs.elementa.constraints.animation.Animations
import cc.polyfrost.oneconfig.libs.elementa.dsl.*
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture

object ScreenshotPreview {

    private var currentWindow: Window? = null

    @SubscribeEvent
    fun renderScreenshot(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        this.currentWindow?.draw(UMatrixStack.Compat.get())
    }

    fun newCapture(image: BufferedImage) {
        this.currentWindow = Window(ElementaVersion.V2)
        this.instantiateComponents(image)
    }

    private fun instantiateComponents(image: BufferedImage) {
        val imageComponent = UIImage(CompletableFuture.completedFuture(image)).constrain {
            x = 0.pixels()
            y = 0.pixels()
            width = RelativeConstraint(1.0F)
            height = RelativeConstraint(1.0F)
        } childOf currentWindow!!

        val style = PatcherConfig.previewAnimationStyle
        val scale = PatcherConfig.previewScale / 5
        val strategy = Animations.OUT_QUINT
        val time = 1.5F
        when (style) {
            0 -> {
                imageComponent.animate {
                    setWidthAnimation(strategy, time, RelativeConstraint(scale))
                    setHeightAnimation(strategy, time, RelativeConstraint(scale))
                    setXAnimation(strategy, time, 10.pixels(true))
                    setYAnimation(strategy, time, 10.pixels(true))
                    onComplete { slideOutAnimation(imageComponent) }
                }
            }

            1 -> {
                imageComponent.constrain {
                    width = RelativeConstraint(scale)
                    height = RelativeConstraint(scale)
                    x = RelativeConstraint(1.0F)
                    y = 10.pixels(true)
                }.animate {
                    setXAnimation(strategy, time, 10.pixels(true))
                    onComplete { slideOutAnimation(imageComponent) }
                }
            }

            2 -> {
                imageComponent.constrain {
                    width = RelativeConstraint(scale)
                    height = RelativeConstraint(scale)
                    x = 10.pixels(true)
                    y = 10.pixels(true)
                }.animate {
                    onComplete { fakeAnimation(imageComponent) }
                }
            }
        }
    }

    private fun fakeAnimation(container: UIComponent) {
        container.animate {
            setColorAnimation(
                Animations.IN_OUT_CIRCULAR,
                1.0F,
                Color.WHITE.toConstraint(),
                PatcherConfig.previewTime.toFloat()
            ).onComplete { currentWindow = null }
        }
    }

    private fun slideOutAnimation(container: UIComponent) {
        container.animate {
            setXAnimation(
                Animations.IN_OUT_CIRCULAR,
                1.0F,
                RelativeConstraint(2.0F),
                PatcherConfig.previewTime.toFloat()
            ).onComplete { currentWindow = null }
        }
    }
}