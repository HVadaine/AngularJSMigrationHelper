
package ProjectStructureGraphBuilder;

import javafx.scene.control.Label;

public class TitledPaneCell extends Cell {

	public TitledPaneCell(String id) {
		super(id);

		Label view = new Label(id);

		setView(view);

	}

}