heights = 5:15;
means_d4 = [509.6,378.9,765.2,481.7,870.2,903.0,570.2,543.5,407.6,376.0,369.9];
fig = figure;
plot(heights, means_d4, 'r+-', 'LineWidth', 2);
xlabel 'Maximum Stack Height'
ylabel 'Consecutive Tetrominoes Placed'
saveas(fig, 'height_v_time_d4.pdf');
close all

% means_d5 = [509.6,378.9,765.2,481.7,870.2,903.0,570.2,543.5,407.6,376.0,369.9];
% fig = figure;
% plot(heights, means_d5, 'b+-', 'LineWidth', 2);
% xlabel 'Maximum Stack Height'
% ylabel 'Consecutive Tetrominoes Placed'
% saveas(fig, 'figures/height_v_time_d5.pdf');
