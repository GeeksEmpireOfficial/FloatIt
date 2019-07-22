package net.geekstools.floatshort.PRO.Widget.NavAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.imageview.customshapes.ShapesImage;

import java.util.Arrays;
import java.util.Comparator;

public class WidgetSectionedGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;
    private final Context context;
    FunctionsClass functionsClass;
    private boolean validate = true;
    private int sectionResourceId;
    private LayoutInflater layoutInflater;
    private RecyclerView.Adapter baseAdapter;
    private SparseArray<Section> sectionSparseArray = new SparseArray<Section>();
    private RecyclerView recyclerView;

    public WidgetSectionedGridRecyclerViewAdapter(Context context, int sectionResourceId, RecyclerView recyclerView, RecyclerView.Adapter baseAdapter) {
        functionsClass = new FunctionsClass(context);

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sectionResourceId = sectionResourceId;
        this.baseAdapter = baseAdapter;
        this.context = context;
        this.recyclerView = recyclerView;

        this.baseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                validate = WidgetSectionedGridRecyclerViewAdapter.this.baseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                validate = WidgetSectionedGridRecyclerViewAdapter.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                validate = WidgetSectionedGridRecyclerViewAdapter.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                validate = WidgetSectionedGridRecyclerViewAdapter.this.baseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        final GridLayoutManager layoutManager = (GridLayoutManager) (this.recyclerView.getLayoutManager());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isSectionHeaderPosition(position)) ? layoutManager.getSpanCount() : 1;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(context).inflate(sectionResourceId, parent, false);
            return new SectionViewHolder(view);
        } else {
            return baseAdapter.onCreateViewHolder(parent, typeView - 1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder) sectionViewHolder).appName.setText(sectionSparseArray.get(position).appName);
            ((SectionViewHolder) sectionViewHolder).appName.setTextColor(PublicVariable.themeLightDark ? context.getColor(R.color.dark) : context.getColor(R.color.light));
            ((SectionViewHolder) sectionViewHolder).appIcon.setImageDrawable(sectionSparseArray.get(position).appIcon);
        } else {
            try {
                baseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : baseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    public void setSections(Section[] sections) {
        sectionSparseArray.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                int comparison = 1;
                try {
                    comparison = Integer.compare(o.firstPosition, o1.firstPosition);
                } catch (Exception e) {
                    comparison = 0;
                }
                return comparison;
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            this.sectionSparseArray.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < sectionSparseArray.size(); i++) {
            if (sectionSparseArray.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
//            return RecyclerView.NO_POSITION;
            return RecyclerView.SCROLLBAR_POSITION_LEFT;
        }

        int offset = 0;
        for (int i = 0; i < sectionSparseArray.size(); i++) {
            if (sectionSparseArray.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return sectionSparseArray.get(position) != null;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - sectionSparseArray.indexOfKey(position)
                : baseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (validate ? baseAdapter.getItemCount() + sectionSparseArray.size() : 0);
    }

    public static class Section {
        int firstPosition;
        int sectionedPosition;

        String appName;
        Drawable appIcon;

        public Section(int firstPosition, String appName, Drawable appIcon) {
            this.firstPosition = firstPosition;

            this.appName = appName;
            this.appIcon = appIcon;
        }

        public String getTitle() {
            return this.appName;
        }

        public Drawable getAppIcon() {
            return this.appIcon;
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        ShapesImage appIcon;
        TextView appName;

        public SectionViewHolder(View view) {
            super(view);
            appIcon = (ShapesImage) view.findViewById(R.id.appIcon);
            appName = (TextView) view.findViewById(R.id.appName);

            appIcon.setShapeDrawable(functionsClass.shapesDrawables());
        }
    }

}