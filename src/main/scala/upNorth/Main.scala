package upNorth

import cats.data.{NonEmptyList, ValidatedNel}
import cats.implicits._
import outwatch.dom._
import outwatch.util.Store
import rxscalajs.Observable
import scala.scalajs.js.JSApp

object Main extends JSApp {

  type State = List[String]
  val initialState: State = List.empty

  sealed trait Action
  case object AddNewItem extends Action
  case class Update(index: Int, item: String) extends Action

  def reducer(old: State, action: Action): State = action match {
    case AddNewItem => old :+ ""
    case Update(index, item) => old.updated(index, item)
  }

  val store = Store(initialState, reducer)

  def itemView(index: Int, item: String): VNode = {
    li(
      input(
        placeholder := "Enter item",
        inputString(s => Update(index, s)) --> store
      )
    )
  }

  def itemListView(list: State): List[VNode] =
    list.zipWithIndex.map {
      case (item, index) => itemView(index, item)
    }



  def validateState(list: List[String]): ValidatedNel[Error, List[String]] =
    list.traverseU(Validation.validateItem)

  def validView(list: List[String]): VNode = button(s"Submit ${list.length} items!")

  def invalidView(errors: NonEmptyList[Error]): VNode = div(
    h3("The following errors occured"),
    ul(
      errors.map(e => li(e.toString)).toList: _*
    ),
    button("Submit", disabled := true)
  )

  val validatedView: Observable[VNode] = store.map { state =>
    validateState(state).fold(invalidView, validView)
  }

  val view: VNode = div(
    button(
      "Add new Item",
      click(AddNewItem) --> store
    ),
    ul(
      children <-- store.map(itemListView)
    ),
    div(child <-- validatedView)
  )



  def main(): Unit = {
    OutWatch.render("#app", view)
  }
}
